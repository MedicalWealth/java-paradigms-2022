"use strict"

function Operation(evaluateImpl, operation, diffImpl, ...operands) {
    this.evaluate = (x, y, z) => evaluateImpl(...operands.map(op => op.evaluate(x, y, z)));
    this.toString = function () {
        return operands.map(op => op.toString()).join(" ") + " " + operation;
    }
    this.prefix = function () {
        return "(" + operation + " " + operands.map(op => op.prefix()).join(' ') + ")";
    }
    this.postfix = function () {
        return "(" + operands.map(op => op.postfix()).join(' ') + " " + operation + ")";
    }
    this.diff = function (variable) {
        return diffImpl(variable, ...operands);
    }
}

function SumSqN(arity, operation, ...operands) {
    return new Operation(
        (...ops) => ops.reduce((a, b) => a + b * b, 0),
        operation,
        (variable, ...ops) => sumSqNDiffImpl(variable, ops),
        ...operands
    );
}

function Sumsq2(...ops) {
    return new SumSqN(
      2, "sumsq2", ...ops
    );
}

function Sumsq3(...ops) {
    return new SumSqN(
        3, "sumsq3", ...ops
    );
}

function Sumsq4(...ops) {
    return new SumSqN(
        4, "sumsq4", ...ops
    );
}

function Sumsq5(...ops) {
    return new SumSqN(
        5, "sumsq5", ...ops
    );
}

function Exp(op) {
    return new Operation(
        (op) => Math.exp(op),
        "exp",
        (v, op) => new Multiply(op, new Exp(op)),
        op
    );
}

const sumexpDiffImpl =(v, ops) => {
    return ops.reduce((a, b) => new Add(new Multiply(b.diff(v), new Exp(b)), a), ZERO);
}

function Sumexp(...operands) {
    return new Operation(
        (...ops) => sumexpEvalImpl(...ops),
        "sumexp",
        (v, ...ops) => sumexpDiffImpl(v, ops),
        ...operands
    );
}

const sumexpEvalImpl = (...ops) => ops.reduce((a, b) => a + Math.exp(b), 0);

function LSE(...operands) {
    return new Operation(
        (...ops) => Math.log(sumexpEvalImpl(...ops)),
        "lse",
        (v, ...ops) => new Divide(sumexpDiffImpl(v, ops), new Sumexp(...operands)),
        ...operands
    );
}

const TWO = new Const(2);

const sumSqNDiffImpl = (variable, ops) => {
    return ops.reduce((a, b) => new Add(new Multiply(b, new Multiply(b.diff(variable), TWO)), a),
        new Const(0));
}

function DistanceN(arity, operation, ...operands) {
    return new Operation(
        (...ops) => Math.sqrt(ops.reduce((a, b) => a + b * b, 0)),
        operation,
        (variable, ...ops) => new Divide(sumSqNDiffImpl(variable, ops), new Multiply(TWO, new DistanceN(arity, operation, ...operands))),
        ...operands
    );
}

function Distance2(...ops) {
    return new DistanceN(
      2, "distance2", ...ops
    );
}

function Distance3(...ops) {
    return new DistanceN(
        3, "distance3", ...ops
    );
}

function Distance4(...ops) {
    return new DistanceN(
        4, "distance4", ...ops
    );
}

function Distance5(...ops) {
    return new DistanceN(
        5, "distance5", ...ops
    );
}


function Add(operand1, operand2) {
    return new Operation(
        (a, b) => a + b,
        "+",
        (v, op1, op2) => new Add(op1.diff(v), op2.diff(v)),
        operand1,
        operand2
    );
}

function Subtract(operand1, operand2) {
    return new Operation(
        (a, b) => a - b,
        "-",
        (v, op1, op2) => new Subtract(op1.diff(v), op2.diff(v)),
        operand1,
        operand2
    );
}

function Multiply(operand1, operand2) {
    return new Operation(
        (a, b) => a * b,
        "*",
        (v, op1, op2) => new Add(new Multiply(op1, op2.diff(v)), new Multiply(op1.diff(v), op2)),
        operand1,
        operand2
    );
}

function Divide(operand1, operand2) {
    return new Operation(
        (a, b) => a / b,
        "/",
        (v, op1, op2) => new Divide(new Subtract(new Multiply(op1.diff(v), op2), new Multiply(op1, op2.diff(v))),
            new Multiply(op2, op2)),
        operand1,
        operand2
    );
}


function Negate(operand) {
    return new Operation(
        (a) => - a,
        "negate",
        (v, op) => new Negate(op.diff(v)),
        operand
    );
}

const ONE = new Const(1);
const ZERO = new Const(0);

function Const(val) {
    this.evaluate = function () {
        return val;
    }
    this.diff = function () {
        return ZERO;
    }
    this.toString = function () {
        return val.toString();
    }
    this.prefix = function () {
        return val.toString();
    }
    this.postfix = function () {
        return val.toString();
    }
}

function Variable(str) {
    this.evaluate = function (x, y, z) {
        if (str === "x") {
            return x;
        } else if (str === "y") {
            return y;
        } else if (str === "z") {
            return z;
        }
    }
    this.diff = function (v) {
        if (str === v) {
            return ONE;
        } else {
            return ZERO;
        }
    }
    this.toString = function () {
        return str;
    }
    this.prefix = function () {
        return str;
    }
    this.postfix = function () {
        return str;
    }
}

const variables = ["x", "y", "z"];
const operations = {
    "+" : [Add, 2],
    "-" : [Subtract, 2],
    "*" : [Multiply, 2],
    "/" : [Divide, 2],
    "negate" : [Negate, 1],
    "distance2" : [Distance2, 2],
    "distance3" : [Distance3, 3],
    "distance4" : [Distance4, 4],
    "distance5" : [Distance5, 5],
    "sumsq2" : [Sumsq2, 2],
    "sumsq3" : [Sumsq3, 3],
    "sumsq4" : [Sumsq4, 4],
    "sumsq5" : [Sumsq5, 5],
    "lse" : [LSE, -1],
    "sumexp" : [Sumexp, -1]
}

const parse = (str) => {
    let array = str.split(/(\s+)/).filter(str => str.trim().length > 0);
    let stack = [];
    for (const v of array) {
        if (operations[v] !== undefined) {
            let [operation, arity] = operations[v];
            let operands = stack.splice(stack.length - arity);
            stack.push(new operation(...operands));
        } else if (variables.includes(v)) {
            stack.push(new Variable(v));
        } else {
            stack.push(new Const(parseFloat(v)));
        }
    }
    return stack.pop();
}

const createError = (ToCreate, name, ToProto) => {
    ToCreate.prototype = Object.create(ToProto.prototype);
    ToCreate.prototype.name = name;
    ToCreate.prototype.constructor = ToCreate;
}


function ParseError(message) {
    Error.call(this, message);
    this.message = message;
}
createError(ParseError, "ParseError", Error);

function ArityError(operation, found_operands) {
    ParseError.call(this, "Got operation " + operation + ", but found " + found_operands + " operands");
    this.operation = operation;
    this.operands = found_operands;
}
createError(ArityError, "ArityError", ParseError);

function ClosingBracketError(position) {
    ParseError.call(this, "Expected closing bracket in pos = " + position);
    this.position = position;
}
createError(ClosingBracketError, "ClosingBracketError", ParseError);

function OperationError(position, message) {
    ParseError.call(this, "Expected operation in pos = " + position + ", but found " + message);
    this.position = position;
}
createError(OperationError, "OperationError", ParseError);

const parsePrefix = (str) => {
    return parseImpl(str, parsePrefixImpl);
}

const parsePostfix = (str) => {
    return parseImpl(str, parsePostfixImpl);
}

const parseImpl = (str, parse) => {
    let expr_tokens = str.split(/(\()|(\))|(\s+)/).filter(s => s && s.trim().length > 0);
    let result = parse(expr_tokens);
    let len = expr_tokens.length;
    if (expr_tokens.length !== 0) {
        throw new OperationError(len - expr_tokens.length, "EOF");
    }
    return result;
}

const getOperation = (operand) => {
    return operations[operand];
}

const checkError = (arity, parsed_operands, array, operation, length) => {
    if (arity !== -1 && arity !== parsed_operands.length) {
        throw new ArityError(operation, parsed_operands.length);
    }
    if (array.shift() !== ')') {
        throw new ClosingBracketError(length - array.length);
    }
}

const getConstOrVariable = (operand, array, length) => {
    if (variables.includes(operand)) {
        return new Variable(operand);
    } else {
        if (isNaN(operand)) {
            throw (operations[operand] === undefined && operand !== ')'
                ? new ParseError("Incorrect token = " + operand)
                : new OperationError(length - array.length, operand));
        }
        return new Const(parseFloat(operand));
    }
}

const parsePrefSufImpl = (array, isPref, length) => {
    if (array[0] === '(') {
        array.shift();
        let operation, arity;
        if (isPref) {
            [operation, arity] = getOperation(array.shift());
        }
        let parsed_operands = [];
        while (isPref && array.length > 0 && array[0] !== ')' ||
               !isPref && operations[array[0]] === undefined) {
            parsed_operands.push(parsePrefSufImpl(array, isPref, length));
        }
        if (!isPref) {
            [operation, arity] = getOperation(array.shift());
        }
        let result = new operation(...parsed_operands);
        checkError(arity, parsed_operands, array, operation.name, length);
        return result;
    } else {
        return getConstOrVariable(array.shift(), array, length);
    }
}

const parsePrefixImpl = (array) => {
    return parsePrefSufImpl(array, true, array.length);
}

const parsePostfixImpl = (array) => {
    return parsePrefSufImpl(array, false, array.length);
}