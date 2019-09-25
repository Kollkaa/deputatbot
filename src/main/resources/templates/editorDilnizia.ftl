<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">
<@c.page>
    <div>
        <form action="/okrugcity" method="post">
            <#attempt>
                <div><a>-- Номер округа НДУ ${dilnizias.getNumber()}</a></div><
                <#recover>

            </#attempt>
            <div><label>НДУ<input type="number" name="ndunumber" value="${dilnizias.getOkrugNdu().getNumber()}"></label></div>
            <div><label>ОБЛАСТЬ<input type="number" name="oblnumber" value="${dilnizias.getOkrugObl().getNumber()}"></label></div>
            <div><label>МІСЬКРАДА ОТГ<input type="number" name="citynumber" value="${dilnizias.getOkrugCity().getNumber()}"></label></div>
            <input type="hidden" value="${dilnizias.getId()?c}" name="dilniziaId">
            <input type="hidden" value="${_csrf.token}" name="_csrf">
            <button type="submit">Сохранить</button>
        </form>
    </div>

</@c.page>