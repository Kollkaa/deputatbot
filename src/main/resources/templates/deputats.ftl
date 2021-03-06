<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">
<@c.page>

    <div class="big-banner">
        <div class="form-row">
            <div class="form-group col-md-6">
                <form method="get" action="/deputats" class="form-inline">
                    <input type="text" name="filter" class="form-control" value="${filter?ifExists}" placeholder="Search by name">
                    <button type="submit" class="btn btn-primary ml-2">Искать</button>
                </form>
            </div>
        </div>




        <div class="card-columns " >
            <#list deputats as deputat>
                <div class="card my-3 " >
                    <div class="m-2">
                        <span>${deputat.name}</span>
                        <span>${deputat.surname}</span>
                        <span>${deputat.partion}</span>
                        <i class="m-1">${deputat.getPartia()}</i>
                        <div class="card-footer text-muted">
                            <a>${deputat.typeOk}
                                <#attempt>
                                    <a>  Номер округа - ${okrugNduRepo.findByDeputat(deputat).getNumber()}</a>
                                    <#recover>
                                        <#attempt>
                                            <a>Номер округа - ${deputat.getOkrugObl().getNumber()}</a>
                                            <#recover>
                                                <#attempt>
                                                    <a>Номер округа - ${deputat.getOkrugCity().getNumber()}</a>
                                                    <#recover>
                                                        okrug haven`t choose
                                                </#attempt>
                                        </#attempt>
                                </#attempt>

                            <div><a href="/deputats/${deputat.getId()?c}">edit</a></div>
                        </div>
                    </div>


                </div>

            <#else>
                No message
            </#list>
        </div>
    </div>
</@c.page>