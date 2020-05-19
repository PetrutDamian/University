var dictionary = new Map()
$(function () {
    $(".vertical").each(function (index,value) {
        attachSortEventVerticalHeaderTable($(value));
    });
    $(".horizontal").each(function (index,value) {
        attachSortEventHorizontalHeaderTable($(value));
    });
});
function less(data1,data2){
    if (!isNaN(data1)) {
        data1 = Number(data1);
        data2 = Number(data2);
    }
    return data1 < data2;
}
function greater(o1,o2){
    return (!less(o1,o2));
}

function attachSortEventHorizontalHeaderTable(table) {
    var body = table.children("tbody");
    var hr = body.children(":first");
    hr.children("th").each(function (index, value) {
        $(value).attr("mod", "none");
        $(this).click(function () {
            var compare = less;
            if ($(this).attr("mod") == "none") {
                $(this).parent().children("th").attr("mod", "none");
                $(this).attr("mod", "asc");
            } else {
                if ($(this).attr("mod") == "asc") {
                    $(this).attr("mod", "desc");
                    compare = greater;
                } else {
                    $(this).attr("mod", "asc");
                }
            }
            var tbody = $(this).parent().parent();
            var len = tbody.children("tr").length;
            var index1 = $(this).index();
            for (let i = 1; i < len - 1; i++)
                for (let j = i + 1; j < len; j++) {
                    var el1 = tbody.children("tr:eq(" + i + ")").children("td:eq(" + index1 + ")").html();
                    var el2 = tbody.children("tr:eq(" + j + ")").children("td:eq(" + index1 + ")").html();
                    if (!compare(el1, el2))
                        tbody.children("tr:eq("+i+")").before(tbody.children("tr:eq("+j+")"));
                }
        });
    });
}
function attachSortEventVerticalHeaderTable(table){
    var body = table.children("tbody");
    body.children("tr").each(function (index,value) {
        let index1 = index;
        var q = $(value).children("th").each(function (index2,value2) {
            $(value2).attr("mod", "none");
            $(this).click(function () {
                var compare = less;
                if($(this).attr("mod")=="none"){
                    $(this).parent().parent().find("th").attr("mod","none");
                    $(this).attr("mod","asc");
                }
                else{
                    if($(this).attr("mod")=="asc")
                    {
                        $(this).attr("mod", "desc");
                        compare = greater;
                    }else {
                        $(this).attr("mod", "asc");
                    }
                }
                var len = $(this).parent()[0].cells.length;
                var index1 = $(this).parent().index();
                for (let i = 1; i < len - 1; i++)
                    for (let j = i + 1; j < len; j++) {
                        var el1 = $(this).parent().children("td:eq("+(i-1)+")").html();
                        var el2 = $(this).parent().children("td:eq("+(j-1)+")").html();
                        if (!compare(el1, el2))
                            swapColumns(table,i,j);
                    }
            });
        });
    });
}
function swapColumns(table, col1, col2){
    var rows = table.children().children();
    rows.each(function (index,value) {
        let c1 = $(this).children().eq(col1);
        let c2 = $(this).children().eq(col2);
        let aux = c1.html();
        c1.html(c2.html());
        c2.html(aux);
    })
}