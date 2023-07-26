(defn parse [mapObj expr]
  (cond
    (list? expr) (apply (mapObj (first expr)) (mapv (fn [op] (parse mapObj op)) (rest expr)))
    (number? expr) ((mapObj 'const) expr)
    (symbol? expr) ((mapObj 'var) (name expr))))

(defn makeParser [mapObj] (fn [str] (parse mapObj (read-string str))))

; task 10
(defn op [f] 
  (fn [& x] 
    (fn [map] (apply f (mapv (fn [value] (value map)) x)))))

(defn divideImpl [& x] 
  (cond (= (count x) 1) (/ 1 (double (first x))) 
        :else (reduce (fn [x y] (/ (double x) (double y))) x)))

(defn sumexpImpl [& x] (apply + (mapv (fn [val] (Math/exp val)) x)))
(defn lseImpl [& x] (Math/log (apply sumexpImpl x)))

(def add (op +))
(def subtract (op -))
(def multiply (op *))
(def divide (op divideImpl))
(def sumexp (op sumexpImpl))
(def negate subtract)
(def lse (op lseImpl))

(defn constant [x] (fn [map] x))
(defn variable [str] (fn [map] (get map str)))

(def operationsFunc {'+ add '- subtract 'negate negate '* multiply '/ divide 'const constant 'var variable 'sumexp sumexp 'lse lse})
(def parseFunction (makeParser operationsFunc))

; task 11
(load-file "proto.clj")
(def evaluate (method :evaluate))
(def toString (method :toString))
(def toStringPostfix (method :toStringPostfix))
(def toStringInfix (method :toStringInfix))
(def diff (method :diff))

(defn join [delimeter strs toStr] (clojure.string/join delimeter (mapv (fn [s] (toStr s)) strs)))

(defn Operation
  [evaluateImpl oper diffImpl & ops]
  {:evaluate (fn [this map] (apply evaluateImpl (mapv (fn [op] (evaluate op map)) ops)))
   :toString (fn [this] (str "(" oper " " (join " " ops toString) ")"))
   :toStringPostfix (fn [this] (str "(" (join " " ops toStringPostfix) " " oper ")"))
   :toStringInfix (fn [this] (if (= (count ops) 1)
                               (str oper " " (toStringInfix (first ops)))
                               (str "(" (join (str " " oper " ") ops toStringInfix) ")")))
   :diff (fn [this var] (apply diffImpl var ops))})

(defn makeOperation [f oper diffImpl]
  (fn [& operands] (apply Operation f oper diffImpl operands)))

(declare Add Subtract Multiply Divide Constant Meansq RMS)

(defn meansqImpl [& ops] (/ (apply + (mapv (fn [op] (* op op)) ops)) (count ops)))
(defn rmsImpl [& ops] (Math/sqrt (apply meansqImpl ops)))

(defn meansqDiffImpl [v & ops] (Divide (apply Add (mapv (fn [op] (Multiply (Constant 2) op (diff op v))) ops)) (Constant (count ops))))
(defn rmsDiffImpl [v & ops] (Divide (apply meansqDiffImpl v ops) (Multiply (Constant 2) (apply RMS ops))))

(defn multiplyDiffImpl [v & ops] 
  (let [g (apply Multiply (rest ops)) f (first ops)]  
    (cond (= 1 (count ops)) (diff f v) 
          :else (Add (Multiply f (diff g v)) (Multiply (diff f v) g)))))
(defn divideDiffImpl [v & ops] 
  (let [g (apply Multiply (rest ops)) f (first ops)] 
    (cond (= 1 (count ops)) (diff (Divide (Constant 1) f) v) 
          :else (Divide (Subtract (Multiply (diff f v) g) (Multiply f (diff g v))) 
                        (Multiply g g)))))
(defn addDiffImpl [v & ops] (apply Add (mapv (fn [op] (diff op v)) ops)))
(defn subtractDiffImpl [v & ops] (apply Subtract (mapv (fn [op] (diff op v)) ops)))

(def Add (makeOperation + "+" addDiffImpl))
(def Subtract (makeOperation - "-" subtractDiffImpl))
(def Multiply (makeOperation * "*" multiplyDiffImpl))
(def Divide (makeOperation divideImpl "/" divideDiffImpl))
(def Negate (makeOperation - "negate" subtractDiffImpl))
(def Meansq (makeOperation meansqImpl "meansq" meansqDiffImpl))
(def RMS (makeOperation rmsImpl "rms" rmsDiffImpl))

(defn bool2val [b] (if b 1 0))

(defn andImpl [op1 op2] (bool2val (and (> op1 0) (> op2 0))))
(defn orImpl [op1 op2] (bool2val (or (> op1 0) (> op2 0))))
(defn xorImpl [op1 op2] (bool2val (not (= (> op1 0) (> op2 0)))))
(defn notImpl [op1] (bool2val (not (> op1 0))))

(def And (makeOperation andImpl "&&" nil))
(def Or (makeOperation orImpl "||" nil))
(def Xor (makeOperation xorImpl "^^" nil))
(def Not (makeOperation notImpl "!" nil))

(def Constant
  (fn [val]
    {:evaluate (fn [this map] val)
     :toString (fn [this] (str val))
     :toStringPostfix (fn [this] (str val)) 
     :toStringInfix (fn [this] (str val))
     :diff (fn [this var] (Constant 0))}
    ))

(defn get_lower [s] (clojure.string/lower-case (subs s 0 1)))

(def Variable
  (fn [v]
    {:evaluate (fn [this map] (get map (get_lower v)))
     :toString (fn [this] v)
     :toStringPostfix (fn [this] v)
     :toStringInfix (fn [this] v)
     :diff (fn [this var] (cond (= (get_lower v) var) (Constant 1) :else (Constant 0)))}
    ))

(def operationsObj {'+ Add '- Subtract 'negate Negate 
                    '* Multiply '/ Divide 'const Constant
                    (symbol "&&") And (symbol "||") Or (symbol "^^") Xor (symbol "!") Not
                    'var Variable 'rms RMS 'meansq Meansq})
(def parseObject (makeParser operationsObj))

;task 12
(load-file "parser.clj")
(def *all-chars (mapv char (range 0 256)))
(defn *chars [p] (+char (apply str (filter p *all-chars))))
(def *letter (*chars #(Character/isLetter %)))
(def *digit (*chars #(Character/isDigit %)))
(def *space (*chars #(Character/isWhitespace %)))
(def *identifier (+str (+seqf cons *letter (+star (+or *letter *digit)))))
(def *ws (+ignore (+star *space)))
(defn *opt_char [ch] (+opt (+char ch)))
(def *number (+map read-string (+str (+map flatten (+seq (*opt_char "-") (+plus *digit) (*opt_char ".") (+star *digit))))))
(defn *ws_around [parser] (+seqn 0 *ws parser *ws))

(def *const (+map Constant (+seqn 0 (*ws_around *number))))
(def *variable (+map Variable (+seqn 0 (*ws_around *identifier))))

(defn *ignore_char_ws [ch] (+ignore (+seqn 0 (*ws_around (+char ch)))))
(def *open_bracket (*ignore_char_ws "("))
(def *close_bracket (*ignore_char_ws ")"))
(defn *string [s] (apply +seqf (constantly (symbol s)) (mapv +char (mapv str s))))
(defn *operation [op] (*string op))
(defn *operations [& op] (apply +or (mapv *operation op)))

(declare *parseOperation *parsePrime)

(defn *unary_op [op] (+map (operationsObj (symbol op)) (+seqn 1 (*ws_around (*operation op)) (delay *parsePrime))))

(defn make_operand [first_op [operation second_op]] ((operationsObj operation) first_op second_op))

(defn binary_op [expression] 
  (reduce make_operand (first expression) (second expression)))

(def *parseUnaryOp (+or (*unary_op "negate") (*unary_op "!")))

(def *parsePrime (+or *const *parseUnaryOp *variable (+seqn 0 *open_bracket (delay (*parseOperation 0)) *close_bracket)))

(def operations_priority 
  (vector 
   (*operations "^^")
   (*operations "||")
   (*operations "&&")
   (*operations "+" "-")
   (*operations "*" "/")))

(defn *parseNext [priority] (if (= (inc priority) (count operations_priority))
                              (delay *parsePrime)
                              (*parseOperation (inc priority))))

(defn *parseOperation [priority] 
  (+map binary_op (+seq (*parseNext priority) (+star (+seq (operations_priority priority) (*parseNext priority))))))

(def parseObjectInfix (+parser (*parseOperation 0)))