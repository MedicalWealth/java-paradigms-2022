(defn is_every [f] (fn [x] (every? f x)))

(def is_vector_of_numbers (is_every number?))
(def is_matrix (is_every vector?))

(defn is_op [check_func] (fn [xs] (and (is_matrix xs) ((is_every true?) (mapv check_func xs)) (apply = (map count xs)))))
(def is_v_op (is_op is_vector_of_numbers))
(def is_m_op (is_op is_matrix))

(defn cnt [x]
  (cond
    (vector? x) (count x)
    :else 0))

(defn make_op [f, check] (fn [& xs] {:pre [(check xs)]} (apply mapv f xs)))

(def v+ (make_op + is_v_op))
(def v- (make_op - is_v_op))
(def v* (make_op * is_v_op))
(def vd (make_op / is_v_op))

(def m+ (make_op v+ is_m_op))
(def m- (make_op v- is_m_op))
(def m* (make_op v* is_m_op))
(def md (make_op vd is_m_op))

(defn scalar [& xs] (apply + (apply v* xs)))
(defn vect2
  ([x] x)
  ([x, y] (vector (- (* (nth y 2) (nth x 1)) (* (nth y 1) (nth x 2)))
                  (- (* (nth y 0) (nth x 2)) (* (nth x 0) (nth y 2)))
                  (- (* (nth x 0) (nth y 1)) (* (nth y 0) (nth x 1))))))

(defn vect [& x] {:pre [(is_v_op x)]} (reduce vect2 x))

(defn v*s
  ([v] v)
  ([v, & s] (let [S (apply * s)]
              (mapv (fn [x] (* x S)) v))))

(defn m*s
  ([m] m)
  ([m, & s] {:pre [(is_vector_of_numbers s)]} 
   (let [S (apply * s)] 
     (mapv (fn [v] (v*s v S)) m))))

(defn m*v [m, v] (mapv (fn [x] (apply + (v* x v))) m))
(defn transpose [m] (apply mapv vector m))
(defn m*m2
  ([m] m)
  ([m1, m2] {:pre [(== (count (transpose m1)) (count m2))]}
            (transpose (mapv (fn [v] (m*v m1 v)) (transpose m2)))))

(defn m*m [& m] {:pre [(and (is_matrix m) (every? true? (mapv is_matrix m)))]} (reduce m*m2 m))

(defn make_tensor_op [f]
  (fn [& elements] {:pre [(apply = (mapv cnt elements))]}
    (cond
      (every? number? elements) (apply f elements)
      :else (apply mapv (make_tensor_op f) elements))
    )
  )

(def t+ (make_tensor_op +))
(def t- (make_tensor_op -))
(def t* (make_tensor_op *))
(def td (make_tensor_op /))