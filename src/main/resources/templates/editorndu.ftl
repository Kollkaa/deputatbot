<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">
<@c.page>
    <div>
        <form action="/okrugndu" method="post">
            <#attempt>
                <div><a>-- Номер округа НДУ ${okrugndu.getNumber()}</a></div><
                <#recover>

            </#attempt>
            <input type="text" name="deputatname" value="${okrugndu.getDeputat().getName()}">
            <input type="text" name="deputatsurname" value="${okrugndu.getDeputat().getSurname()}">
            <input type="text" name="deputatpartional" value="${okrugndu.getDeputat().getPartion()}">
            <#list partias as partia>
                <div>
                    <label><input type="checkbox" name="${partia}" ${okrugndu.getDeputat().getPartia()}>${partia}</label>
                </div>
            </#list>
            <input type="hidden" value="${okrugndu.getId()?c}" name="okrugId">
            <input type="hidden" value="${_csrf.token}" name="_csrf">
            <button type="submit">Сохранить</button>
        </form>
    </div>

</@c.page>