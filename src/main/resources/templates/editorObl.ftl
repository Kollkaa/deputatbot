<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">
<@c.page>
    <div>
        <form action="/okrugobl" method="post">
            <#attempt>
                <div><a>-- Номер округа НДУ ${okrugobl.getNumber()}</a></div><
                <#recover>

            </#attempt>
            <input type="text" name="deputatname" value="${deputats.findByOkrugObl(okrugobl).getName()}">
            <input type="text" name="deputatsurname" value="${deputats.findByOkrugObl(okrugobl).getSurname()}">
            <input type="text" name="deputatpartional" value="${deputats.findByOkrugObl(okrugobl).getPartion()}">
            <div>
                <h6>Виберіть Партію</h6>
                <select  name="partis">
                    Виберіть потрібний партію
                    <#list partias as partia >
                        <#if partia.GetPartiaName()==okrugobl.getDeputat().getPartia()>
                            <option value="${partia}" selected="selected">${partia.GetPartiaName()}</option>

                        <#else>
                            <option value="${partia}">${partia.GetPartiaName()}</option>
                        </#if>
                    </#list>
                </select>
            </div>

            <input type="hidden" value="${okrugobl.getId()?c}" name="okrugId">
            <input type="hidden" value="${_csrf.token}" name="_csrf">
            <button type="submit">Сохранить</button>
        </form>
    </div>

</@c.page>