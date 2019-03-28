import pl.jaremko.rpn.InfExpression
import pl.jaremko.rpn.RpnExpression
import spock.lang.Specification

class InfExpressionTest extends Specification {

    def "Should properly add 'INF: ' before actual expression"() {
        expect:
            (new InfExpression(expr)).toString() == result

        where:
            expr | result
            "a+b"| "INF: a+b"
    }

    def "Should properly remove invalid characters"() {
        when:
            InfExpression inf = new InfExpression(expr)
            inf.stripInvalid()

        then:
            inf.expression == result

        where:
            expr                | result
            't=~a<x<~b'         | 't=~a<x<~b'
            '( a,+ b)/..[c3'    | '(a+b)/c'
            '~a-~~b<c+d&!p|!!q' | '~a-~~b<c+dpq'
    }

    def "Should properly check expression validity"() {
        when:
            (new InfExpression(expr)).checkValidity()

        then:
            def error = thrown(result)

        where:
            expr            || result
            'a)+(b'         || IllegalArgumentException
            '~a-~~b<c+dpq'  || IllegalArgumentException
            '((((a)'        || IllegalArgumentException
            'a^b*c-d<xpq+x' || IllegalArgumentException
    }

    def "Should properly convert to RPN"() {
        when:
            RpnExpression rpn = (new InfExpression(expr)).getConverted()

        then:
            rpn.expression == result

        where:
            expr                | result
            'a+b+(~a-a)'        | 'ab+a~a-+'
            'x=~~a+b*c'         | 'xa~~bc*+='
            't=~a<x<~b'         | 'ta~x<b~<='
            'x=a=b=c'           | 'xabc==='
            'x=~a*b/c-d+e%~f'   | 'xa~b*c/d-ef~%+='
            'x=a^b^c'           | 'xabc^^='
            'x=a=b=c^d^e'       | 'xabcde^^==='
            'x=(a=(b=c^(d^e)))' | 'xabcde^^==='
            'x=~~~~a'           | 'xa~~~~='
            'x=~(~(~(~a)))'     | 'xa~~~~='
            'x=a+(b-c+d)'       | 'xabc-d++='
            'x=a+(((a-b)+c))'   | 'xaab-c++='
    }
}
