# 컴포넌트 소개
## VueComponents
화면에 비춰지는 뷰의 단위를 쪼개어 재활용이 가능한 형태로 관리하는 것이 컴포넌트
# 컴포넌트 등록
Vue.component('tagName', {}) 형태로 전역으로 등록한다
[테스트](컴포넌트%20등록/index.html)
# 전역 & 지역 컴포넌트 등록
Vue.component('tagName', {}) 형태로 전역으로 등록한다  
`
var cmp = {  
    data: ...,
    template: ...,
    methods: {}
}
new Vue({
    components: {
        'my-cmp' : cmp
    }
))
` 로 지역 등록이 가능하다 [테스트](전역&지역%20컴포넌트%20등록/index.html) 
# 전역 & 지역 컴포넌트 차이점
현재 테스트에서는 #app만 있지만 만약 #app2가 생기고 local-component를 먹히게 하기 싫으면 등록하지 않으면 된다.  
그럴경우엔 전역 컴포넌트만 적용된다