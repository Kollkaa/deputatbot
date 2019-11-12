<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">
<@c.page>
<div>
    <form method="get" action="/feedback/notFound">
        <button type="submit">Показать фидбек "не знайшов" </button>
    </form>
    <form method="get" action="/feedback/notCorect">
        <button type="submit">Показать фидбек "не правильно виводить" </button>

    </form>
    <form method="get" action="/feedback/likeBot">
        <button type="submit">Показать фидбек "нравиться бот" </button>

    </form>
    <form method="get" action="/feedback/anything">
        <button type="submit">Показать фидбек "другое" </button>

    </form>

        <#list feedbacks as feedback>
            <div><label>--${feedback.getFeedback()}  :   ${feedback.getDate()}---${feedback.getTypeFeedback().GetTypeFeedback()}</label></div>
            <#else >
            пока ещё отзывов нету
        </#list>

</div>
</@c.page>