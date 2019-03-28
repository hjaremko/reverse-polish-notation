import pl.jaremko.rpn.InfExpression
import pl.jaremko.rpn.RpnExpression
import spock.lang.Specification

class RpnExpressionTest extends Specification {

    def "Should properly add 'RPN: ' before actual expression"() {
        expect:
            (new RpnExpression(expr)).toString() == result

        where:
            expr | result
            "ab+"| "RPN: ab+"
    }

    def "Should properly remove invalid characters"() {
        when:
        RpnExpression rpn = new RpnExpression(expr)
        rpn.stripInvalid()

        then:
        rpn.expression == result

        where:
        expr                | result
        't=~a<x<~b'         | 't=~a<x<~b'
        '( a,b,.).c;-,*'    | 'abc-*'
        '~a-~~b<c+d&!p|!!q' | '~a-~~b<c+dpq'
    }

    def "Should properly check expression validity"() {
        when:
            (new RpnExpression(expr)).checkValidity()

        then:
            def error = thrown(result)

        where:
            expr             | result
            'abc++def++g+++' | IllegalArgumentException
            'ab'             | IllegalArgumentException
    }

    def "Should properly convert to INF"() {
        when:
            InfExpression inf = (new RpnExpression(expr)).getConverted()

        then:
            inf.expression == result

        where:
            expr              | result
            'xabc**='         | 'x=a*(b*c)'
            'ab+a~a-+'        | 'a+b+(~a-a)'
            'xabc==='         | 'x=a=b=c'
            'xabcdefg++++++=' | 'x=a+(b+(c+(d+(e+(f+g)))))'
            'ab+c+d+e+f+g+'   | 'a+b+c+d+e+f+g'
            'abc++def++g++'   | 'a+(b+c)+(d+(e+f)+g)'
            'xabcde^^==='     | 'x=a=b=c^d^e'
            'xa~~~~~~='       | 'x=~~~~~~a'
            'xa~~~~='         | 'x=~~~~a'
            'xabc-d++='       | 'x=a+(b-c+d)'
            'xaab-c++='       | 'x=a+(a-b+c)'
    }
}
