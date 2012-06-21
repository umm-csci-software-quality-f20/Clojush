(ns clojush.instructions.string
  (:use [clojush.pushstate]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; instructions for strings

(define-registered 
  string_concat
  (fn [state]
    (if (not (empty? (rest (:string state))))
      (if (>= max-string-length (+ (count (stack-ref :string 1 state))
                                   (count (stack-ref :string 0 state))))
        (push-item (str (stack-ref :string 1 state)
                        (stack-ref :string 0 state))
                   :string
                   (pop-item :string (pop-item :string state)))
        state)
      state)))

(define-registered 
  string_take
  (fn [state]
    (if (and (not (empty? (:string state)))
             (not (empty? (:integer state))))
      (push-item (apply str (take (stack-ref :integer 0 state)
                                  (stack-ref :string 0 state)))
                 :string
                 (pop-item :string (pop-item :integer state)))
      state)))

(define-registered 
  string_length
  (fn [state]
    (if (not (empty? (:string state)))
      (push-item (count (stack-ref :string 0 state))
                 :integer
                 (pop-item :string state))
      state)))

(define-registered
  string_atoi
  (fn [state]
    (if (not (empty? (:string state)))
      (try (pop-item :string
                     (push-item (Integer/parseInt (top-item :string state))
                                :integer state))
           (catch Exception e state))
      state)))

(define-registered
  string_reverse
  (fn [state]
    (if (empty? (:string state))
      state
      (let [top-string (top-item :string state)]
        (push-item (apply str (reverse top-string))
                   :string
                   (pop-item :string state))))))

(define-registered
  string_parse_to_chars
  (fn [state]
    (if (empty? (:string state))
      state
      (loop [char-list (reverse (top-item :string state))
             loop-state (pop-item :string state)]
        (if (empty? char-list)
          loop-state
          (recur (rest char-list)
                 (push-item (str (first char-list)) :string loop-state)))))))
