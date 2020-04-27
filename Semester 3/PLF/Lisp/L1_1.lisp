;a
(defun f1(l a poz)
(cond ((null l) nil)
((equal (mod poz 2) 0) (cons (car l) (cons a (insert (cdr l) a (+ 1 poz)))))
(t (cons (car l) (insert (cdr l) a (+ poz 1))))

))
(defun f1m(l a)
(insert l a 1)
)

;b

(defun f2(l)
(cond ((null l) nil)
((listp (car l)) (append (f2 (car l)) (f2 (cdr l))))
(t (cons (car l) (f2 (cdr l))))
)
)

(defun f2m(l)
(reverse (f2 l))
)

(defun cmmdc(a b)
(cond ((equal b 0) a)
(t (cmmdc b (mod a b)))
)
)

(defun f3(l)
(cond ((numberp (car l)) (cmmdc (car l) (f3 (cdr l))))
((null l) 0)
((listp (car l)) (cmmdc (f3 (car l)) (f3 (cdr l))))
(t (f3 (cdr l)))
)
)

(defun f4(l e)
(cond ((null l) 0)
((listp (car l)) (+ (f4 (car l) e) (f4 (cdr l) e)))
((equal e (car l)) (+ 1 (f4 (cdr l) e)))
(t (f4 (cdr l) e))
)
)