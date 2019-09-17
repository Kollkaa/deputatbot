<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">
<@c.page>
    <div>
        <form action="/cities" method="post">
            <#attempt>
                <div><a>--  ${city.getName() + " " + city.getTypeCity()}</a></div><

            <input type="text" name="mername" value="${city.getMer().getName()}">
            <input type="text" name="mersurname" value="${city.getMer().getSurname()}">
            <input type="text" name="merpartional" value="${city.getMer().getPartion()}">
                <#recover>

            </#attempt>
            <input type="hidden" value="${city.getId()?c}" name="cityId">
            <input type="hidden" value="${_csrf.token}" name="_csrf">
            <button type="submit">Сохранить</button>
        </form>
    </div>

</@c.page>