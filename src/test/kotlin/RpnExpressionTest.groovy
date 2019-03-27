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
        RpnExpression inf = new RpnExpression(expr)
        inf.stripInvalid()

        then:
        inf.expression == result

        where:
        expr                | result
        't=~a<x<~b'         | 't=~a<x<~b'
        '( a,b,.).c;-,*'    | 'abc-*'
        '~a-~~b<c+d&!p|!!q' | '~a-~~b<c+dpq'
    }

    def "Should properly check expression validity"() {
        when:
        RpnExpression inf = new RpnExpression(expr)

        then:
        inf.isValid() == result

        where:
        expr             | result
        'abc++def++g+++' | false
        'xabcde^^==='    | true
    }

    def "Should properly convert to RPN"() {
        when:
        RpnExpression inf = new RpnExpression(expr)

        then:
        inf.getConverted().expression == result

        where:
        expr              | result
        'xabc**='         | 'x=a*(b*c)'
        'ab+a~a-+'        | 'a+b+(~a-a)'
        'abc++def++g+++'  | 'error'
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
