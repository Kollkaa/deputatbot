<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">
<@c.page>
    <div>
        <form action="/dilnizias" method="post">
            <#attempt>
                <div><a>-- Номер округа НДУ ${dilnizias.getNumber()}</a></div><
                <#recover>

            </#attempt>

            <div><label>НДУ<input type="number" name="ndunumber" value="${dilnizias.getOkrugNdu().getNumber()}"></label></div>
            <div><label>ОБЛАСТЬ<input type="number" name="oblnumber" value="${dilnizias.getOkrugObl().getNumber()}"></label></div>
            <#attempt>
                <div>
                    <label>МІСЬКРАДА ОТГ
                        <input type="number" name="citynumber" value="${dilnizias.getOkrugCity().getNumber()}">
                    </label>
                </div>
                <#recover >
                    <div>
                        <label>МІСЬКРАДА ОТГ
                            <input type="number" name="citynumber" value="1">
                        </label>
                    </div>
            </#attempt>
            <select name="city">
                <#list cities as city>
                    <#attempt >
                        <#if dilnizias.getOkrugCity().getCity()==city>
                            <option value="${city.getName()}" selected="selected">${city.getName()}<option/>
                        <#else >
                            <option value="${city.getName()}">${city.getName()}<option/>
                        </#if>
                        <#recover >
                            <option value="${city.getName()}">${city.getName()}<option/>
                    </#attempt>
                </#list>
            </select>
            <div>
                <label>РЕГІОН
                    <input type="text" name="region" value="${dilnizias.getRegion()}">
                </label>
            </div>
            <input type="hidden" value="${dilnizias.getId()?c}" name="dilniziaId">
            <input type="hidden" value="${_csrf.token}" name="_csrf">
            <button type="submit">Сохранить</button>
        </form>
    </div>

</@c.page>