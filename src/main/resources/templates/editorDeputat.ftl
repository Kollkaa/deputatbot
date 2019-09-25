<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">
<@c.page>
    <div>
        <form action="/deputats" method="post">
                <div><a>--  ${deputat.getName() + " " + deputat.getTypeOk().name()}</a></div><

                <input type="text" name="depname" value="${deputat.getName()}">
                <input type="text" name="depsurname" value="${deputat.getSurname()}">
                <input type="text" name="deppartional" value="${deputat.getPartion()}">
                <input type="number" name="number_okrug" value="${okrug.getNumber()}">


                <div>
                    <h6>Виберіть тип округа</h6>
                    <select  name="typs">
                        Виберіть потрібний тип
                       <#list typeOk as type >
                           <#if type==deputat.getTypeOk()>
                               <option value="${type}" selected="selected">${type.GetTypeOkrug()}</option>

                           <#else>
                               <option value="${type}">${type.GetTypeOkrug()}</option>

                           </#if>
                       </#list>
                    </select>
                </div>
                <div>
                    <h6>Виберіть Партію</h6>
                    <select  name="partis">
                        Виберіть потрібний партію
                        <#list partias as partia >
                            <#if partia.GetPartiaName()==deputat.getPartia()>
                                <option value="${partia}" selected="selected">${partia.GetPartiaName()}</option>

                            <#else>
                                <option value="${partia}">${partia.GetPartiaName()}</option>
                            </#if>
                        </#list>
                    </select>
                </div>



            <input type="hidden" value="${deputat.getId()?c}" name="deputatId">
            <input type="hidden" value="${_csrf.token}" name="_csrf">
            <button type="submit">Сохранить</button>
        </form>
    </div>

</@c.page>