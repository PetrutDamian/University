var identity = undefined;
var blank = undefined;
var n = undefined;
var table = undefined;
$(function () {
    $(document).keydown (function(e) {
        switch (e.keyCode) {
            case 37:
                left();
                break;
            case 38:
                up();
                break;
            case 39:
                right();
                break;
            case 40:
                down();
                break;
        }
    });
    n = 4;
    identity = new Array(n*n);
    for (let i=1;i<n*n;i++)
        identity[i] = i;
    identity[0] = '';
    for (let i=0;i<n*n;i++)
        if(identity[i]=='')
            blank = i;
    populateTable();
        shuffle();

})

function populateTable(){
    table = $("table");
    for(let i=1;i<=n;i++){
        let row = $("<tr></tr>");
        for(let j=1;j<=n;j++){
            let dataCell = $("<td></td>");
            dataCell.html(identity[n*(i-1)+j-1]);
            row.append(dataCell);
        }
        table.append(row);
    }
}
function left(){
    if(blank%n!=0) {//mutare la stanga
        let line = Math.floor((blank ) / n);
        let col = blank%n;

        $($(table.children()[line]).children()[col]).html($($(table.children()[line]).children()[col-1]).html());
        $($(table.children()[line]).children()[col-1]).html("");
        blank -=1;
    }
}
function right(){
    if((blank+1)%n!=0){//right
        let line = Math.floor((blank ) / n);
        let col = blank%n;
        $($(table.children()[line]).children()[col]).html($($(table.children()[line]).children()[col+1]).html());
        $($(table.children()[line]).children()[col+1]).html("");
        blank +=1;
    }
}
function up(){
    if(blank>=n)//up
    {
        let line = Math.floor((blank ) / n);
        let col = blank%n;
        $($(table.children()[line]).children()[col]).html($($(table.children()[line-1]).children()[col]).html());
        $($(table.children()[line-1]).children()[col]).html("");
        blank-=n;

    }
}
function down(){
    if(blank<n*(n-1))//down
    {
        let line = Math.floor((blank ) / n);
        let col = blank%n;
        $($(table.children()[line]).children()[col]).html($($(table.children()[line+1]).children()[col]).html());
        $($(table.children()[line+1]).children()[col]).html("");
        blank+=n;
    }
}

function shuffle(){
    let i =400;
    while (i>0){
        i--;
        let r = Math.random();
        if(0<=r && r<0.25)
            left();
        if(0.25<=r && r<0.5)
            right();
        if (0.5<=r && r<0.75)
            up();
        if(0.75<=r && r<=1)
            down();
    }
}