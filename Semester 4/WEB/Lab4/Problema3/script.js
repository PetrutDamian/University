var identity = ['1','1','2','2','3','3','4','4','5','5','6','6','7','7','8','8'];
var flipped = []
var discovered = []

function shuffle(array){
    var size = array.length
    var aux = Number()
    for (var i = size-1; i>0;i--){
        r = Math.floor(Math.random() * (i+1));
        aux = array[r];
        array[r] = array[i];
        array[i] = aux;
    }
}
function reveal(element, index){
    if (flipped.length <2){
        if (!(flipped.includes(index)) && !(discovered.includes(index))) {
            flipped.push(index);
            document.getElementById("span" + index).style.visibility = "visible";
        }
            if(flipped.length == 2){
                if (identity[flipped[0]] == identity[flipped[1]]){
                    discovered.push(flipped[0]);
                    discovered.push(flipped[1]);
                    flipped = []
                    if (discovered.length == 16){
                        flipped = [1,2,3];
                        alert("Ati castigat");
                    }
                }
                else{
                    function hideBack(){
                        document.getElementById('span'+flipped[1]).style.visibility = "hidden";
                        document.getElementById('span'+flipped[0]).style.visibility = "hidden";
                        flipped = []
                    }
                    setTimeout(hideBack,500);

                }

            }
    }
}
function createMemoryGame(){
    flow = 0;
    shuffle(identity)
    var str = ""
    for(var i=0;i<identity.length;i++)
        str += '<div id="div'+i+'" onclick="reveal(this,'+i+')"><span id="span'+i+'">'+identity[i]+'</span></div>';

    document.getElementById('container').innerHTML = str;
    function hide() {
        for (var i = 0; i < 16; i++)
            document.getElementById('span' + i).style.visibility = "hidden";
    }
    setTimeout(hide,400);
}

