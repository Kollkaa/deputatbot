<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">
<@c.page>

    <div class="big-banner">
        <div class="form-row">
            <div class="form-group col-md-6">
                <form method="get" action="/editer" class="form-inline">
                    <input type="text" name="filter" class="form-control" value="${filter?ifExists}" placeholder="Search by name">
                    <button type="submit" class="btn btn-primary ml-2">Искать</button>
                </form>
                <form method="get" action="/editers" class="form-inline">
                    <button type="submit" class="btn btn-primary ml-2">Создать</button>
                </form>
            </div>
        </div>




        <div class="card-columns " >
            <#list dilnizias as dilnizia>
                <div class="card my-3 " >
                    <div class="m-2">
                        <#attempt>
                            <h1>${dilnizia.okrugNdu.number}</h1>
                            <form method="get" action="/editorndu" class="form-inline">
                                <button type="submit" class="btn btn-primary ml-2">Изменить</button>
                                <input  type="hidden"  name="id" class="form-control" value="${dilnizia.okrugNdu.getId()}" placeholder="Search by name">
                            </form>
                            <#recover>
                                okrug haven`t choose
                        </#attempt>

                        <#attempt>
                            <h1>${dilnizia.okrugObl.number}</h1>
                            <form method="get" action="/editorobl" class="form-inline">
                                <button type="submit" class="btn btn-primary ml-2">Изменить</button>
                                <input  type="hidden"  name="id" class="form-control" value="${dilnizia.okrugObl.getId()}" placeholder="Search by name">
                            </form>
                            <#recover>
                                okrug haven`t choose
                        </#attempt>

                        <#attempt>
                            <h1>${dilnizia.okrugCity.number}</h1>
                            <form method="get" action="/editorscity" class="form-inline">
                                <button type="submit" class="btn btn-primary ml-2">Изменить</button>
                                <input  type="hidden"  name="id" class="form-control" value="${dilnizia.okrugCity.getId()}" placeholder="Search by name">
                            </form>
                            <#recover>
                                okrug haven`t choose
                        </#attempt>

                        <div class="card-footer text-muted">
                            ${dilnizia.region}
                        </div>
                        <form method="get" action="/editors" class="form-inline">
                            <button type="submit" class="btn btn-primary ml-2">Изменить</button>
                            <input  type="hidden"  name="number" class="form-control" value="${dilnizia.number}" placeholder="Search by name">
                        </form>
                    </div>


                </div>

            <#else>
                No message
            </#list>
        </div>
    </div>
</@c.page>