(defun vale(l s)
(cond   ((null l) nil)
        ((equal (length l) 1) 
            (cond ((equal s 2) t)
                    (t nil)
            )
        )
       ((and (< (car l) (cadr l)) (> s 0)) 
            (vale (cdr l) 2)
       )
       ((and (> (car l) (cadr l)) (< s 2))
            (vale (cdr l) 1)
       )
       (t nil)
)
)
(defun vale_m(l)
(vale l 0)
)