<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">
<@c.page>
    <div>
        <form action="/okrugobl" method="post">
            <#attempt>
                <div><a>-- Номер округа бласті ${okrugobl.getNumber()}</a></div><
                <#recover>

            </#attempt>
            <#list deputats.findAllByOkrugObl(okrugobl)as dep>
                <input type="text" name="deputatname" value="${dep.getName()}">
                <input type="text" name="deputatsurname" value="${dep.getSurname()}">
                <input type="text" name="deputatpartional" value="${dep.getPartion()}">
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
            </#list>
            <#if deputats.findAllByOkrugCity(okrugcity).size()==0>
                <form method="post" action="/deputats/addDepo">
                    <input type="text" name="name" value="name">
                    <input type="text" name="surname" value="surname">
                    <input type="text" name="partional" value="partion">
                    <div>
                        <h6>Виберіть Партію</h6>
                        <select  name="partia">
                            Виберіть потрібний партію
                            <#list partias as partia >
                                <option value="${partia}">${partia.GetPartiaName()}</option>
                            </#list>
                        </select>
                    </div>
                    <input type="hidden" value="${okrugobl.getId()?c}" name="id">
                    <input type="hidden" value="${_csrf.token}" name="_csrf">
                    <button type="submit">Сохранить</button>
                </form>

            </#if>


            <input type="hidden" value="${okrugobl.getId()?c}" name="okrugId">
            <input type="hidden" value="${_csrf.token}" name="_csrf">
            <button type="submit">Сохранить</button>
        </form>
    </div>

</@c.page>