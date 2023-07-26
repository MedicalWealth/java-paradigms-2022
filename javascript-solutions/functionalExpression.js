"use strict"

const constant = operation => () => operation();

const operation = operation => (...operands) => (...args) => operation(...operands.map(operand => operand(...args)));

const add = operation((operand1, operand2) => operand1 + operand2);

const subtract = operation((operand1, operand2) => operand1 - operand2);

const multiply = operation((operand1, operand2) => operand1 * operand2);

const divide = operation((operand1, operand2) => operand1 / operand2);

const negate = operation(operand => -1 * operand);

const madd = operation((operand1, operand2, operand3) => operand1 * operand2 + operand3);

const floor = operation(operand => Math.floor(operand));

const ceil = operation(operand => Math.ceil(operand));

const one = constant(() => 1);

const two = constant(() => 2);

const cnst = x => () => x;

const variable = str => (...args) => {
    if (str === "x") {
        return args[0];
    } else if (str === "y") {
        return args[1];
    } else if (str === "z") {
        return args[2];
    }
}

const create_operand = {
    "x" : variable,
    "y" : variable,
    "z" : variable,
    "+" : add,
    "-" : subtract,
    "*" : multiply,
    "/" : divide,
    "negate" : negate,
    "*+" : madd,
    "_" : floor,
    "^" : ceil,
    "one" : one,
    "two" : two
}

const ternary_operations = ["*+"];
const binary_operations = ["+", "-", "*", "/"];
const unary_operations = ["negate", "_", "^"];
const variables = ["x", "y", "z"];
const constants = ["one", "two"];

const parse = (str) => {
    let array = str.split(/(\s+)/).filter(str => str.trim().length > 0);
    let stack = [];
    for (const v of array) {
        if (constants.includes(v)) {
            stack.push(create_operand[v]);
        } else if (ternary_operations.includes(v)) {
            stack.push(create_operand[v](...[stack.pop(), stack.pop(), stack.pop()].reverse()));
        } else if (binary_operations.includes(v)) {
            stack.push(create_operand[v](...[stack.pop(), stack.pop()].reverse()));
        } else if (unary_operations.includes(v)) {
            stack.push(create_operand[v](...[stack.pop()]));
        } else if (variables.includes(v)) {
            stack.push(create_operand[v](v));
        } else {
            stack.push(cnst(parseFloat(v)));
        }
    }
    return stack.pop();
}