(set-logic QF_UF)
(set-info :smt-lib-version 2.0)
(set-info :category "crafted")
(set-info :status sat)
(declare-sort U 0)
(declare-fun f (U) U)
(declare-fun a () U)
(declare-fun b () U)
(assert (= a b))
(assert (= (f a) b))
(assert (= (f b) a))
(check-sat)
