

(defun fa(v1 v2)
(cond
 ((null v1) 0)
 (t (+ (* (car v1) (car v2)) (fa (cdr v1) (cdr v2))))
)
)