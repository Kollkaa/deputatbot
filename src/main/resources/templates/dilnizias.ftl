<#import "parts/common.ftl" as c>
<#include "parts/security.ftl">
<@c.page>

    <div class="big-banner">
        <div class="form-row">
            <div class="form-group col-md-6">
                <div><form method="get" action="/dilniziar" class="form-inline">
                    <input type="text" name="regions" class="form-control" value="${regions?ifExists}" placeholder="Пошук по адресі">
                    <button type="submit" class="btn btn-primary ml-2">Знайти по адресі</button>
                </form>
                fefe
                <form method="get" action="/dilniziaf" class="form-inline">
                    <input type="number" name="number" class="form-control" value="${number?ifExists}" placeholder="Пошук по номеру дільниці">
                    <button type="submit" class="btn btn-primary ml-2">Знайти по номеру дільниці</button>
                </form>
                </div>
                <div>
                <form method="get" action="/dilniziaNdu" class="form-inline">
                    <input type="number" name="number" class="form-control"  placeholder="Пошук по номеру дільниці">
                    <button type="submit" class="btn btn-primary ml-2">Знайти по номеру дільниці</button>
                </form>
                <form method="get" action="/dilniziaObl" class="form-inline">
                    <input type="number" name="number" class="form-control"  placeholder="Пошук по номеру дільниці">
                    <button type="submit" class="btn btn-primary ml-2">Знайти по номеру дільниці</button>
                </form>
                <form method="get" action="/dilniziaCity" class="form-inline">
                    <input type="number" name="number" class="form-control" placeholder="Пошук по номеру дільниці">
                    <button type="submit" class="btn btn-primary ml-2">Знайти по номеру дільниці</button>
                </form>
                </div>
            </div>
        </div>




        <div class="card-columns " >
            <#list dilnizias as dilnizia>
                <div class="card my-3 " >
                    <div class="m-2">
                          <a>${dilnizia.number} Номер дільниці</a>
                          <#attempt>
                                <a>-- Номер округа НДУ ${dilnizia.okrugNdu.getNumber()?ifExists}</a>
                              <#recover>

                          </#attempt>
                          <#attempt>

                                <a>-- Номер округа ОБЛ ${dilnizia.okrugObl.getNumber()?ifExists}</a>
                              <#recover>

                          </#attempt>
                          <#attempt>
                                <a>-- Номер округа ГОРОДА ${dilnizia.okrugCity.getNumber()?ifExists}-- Номер округа ГОРОДА</a>
                              <#recover>

                          </#attempt>
                          <div class="card-footer text-muted">
                            <span>Район дільниці >>> ${dilnizia.getRegion()} </span>

                          </div>
                        <div>
                            <a href="/dilnizias/${dilnizia.getId()?c}">Edit</a>
                        </div>
                    </div>


                </div>
            <#else>
                No message
            </#list>
        </div>
    </div>
</@c.page>