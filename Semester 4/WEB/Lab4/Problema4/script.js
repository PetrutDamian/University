var dictionary = new Map()
function attachSortEventHorizontalHeaderTable(id){
    let table = document.getElementById(id);
    let rows = table.rows;
    let len = rows[0].cells.length;
    for(let i=0;i<len;i++){
        rows[0].cells[i].addEventListener('click',function(){
            sortRows(id,i);
        })
    }
}
function attachSortEventVerticalHeaderTable(id){
    let table = document.getElementById(id);
   let rows = table.rows;
   for(let i=0;i<rows.length;i++){
       let h = table.rows[i].cells[0];
       let rowIndex = h.parentNode.rowIndex;
       rows[i].cells[0].addEventListener('click',function(){
           sortColumns(id,rowIndex);
       });
   }
}
function swapColumns(table, col1, col2){
    let len = table.rows.length;
    for(let i=0;i<table.rows.length;i++){
        let data1 = table.rows[i].cells[col1].innerText;
        let data2 = table.rows[i].cells[col2].innerText;
        table.rows[i].cells[col1].innerHTML = data2;
        table.rows[i].cells[col2].innerHTML = data1;
    }
}
function swapRows(table,row1,row2){
    let data1 = table.rows[row1].innerHTML;
    table.rows[row1].innerHTML = table.rows[row2].innerHTML;
    table.rows[row2].innerHTML = data1;
}
function sortColumns(tableId, rowIndex){
    let compare = manageDictionary(tableId, rowIndex)
    let table =  document.getElementById(tableId);
    let rowLength = table.rows[0].cells.length;
    for(let i=1;i<rowLength-1;i++){
        for(let j=i+1;j<rowLength;j++){
            let data1 = table.rows[rowIndex].cells[i].innerText;
            let data2 = table.rows[rowIndex].cells[j].innerText;
            if(!isNaN(data1)){
                data1 = Number(data1);
                data2 = Number(data2);
            }
            if(!compare(data1,data2))
                swapColumns(table,i,j);
        }
    }
}
function sortRows(tableId, columnIndex){
    let compare = manageDictionary(tableId,columnIndex)
    let table = document.getElementById(tableId);
    for (let i=1;i<table.rows.length-1;i++){
        for(let j=i+1;j<table.rows.length;j++){
            let data1 = table.rows[i].cells[columnIndex].innerText;
            let data2 = table.rows[j].cells[columnIndex].innerText;
            if(!isNaN(data1)){
                data1 = Number(data1);
                data2 = Number(data2);
            }
            if(!compare(data1,data2))
                swapRows(table,i,j);
        }
    }
}

function less(o1, o2){
    return o1<o2;
}
function greater(o1, o2){
    return o1>o2;
}
function manageDictionary(tableID, headerIndex){
    let obj = dictionary.get(tableID)
    if (obj == undefined)
    {
        let value = new Object()
        value.state = 1;
        value.index = headerIndex;
        dictionary.set(tableID,value);
        return less;
    }
    else{
        if(obj.index != headerIndex)
        {
            obj.index = headerIndex;
            obj.state = 1;
            return less;
        }
        else{
            if (obj.state == 1)
                {
                    obj.state = 2;
                    return greater;
                }
            else
            {
                obj.state = 1;
                return less;
            }
        }
    }
}