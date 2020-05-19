function validate(){
    var errors = "";
    var nume = $("#nume1");
    if (nume.val() == "") {
        errors += "Nu ati completat numele!\n";
        nume.css("outline", "red 2px solid");
    }
    else{
        if(!(/^[a-z A-Z]+$/.test(nume.val())))
        {
            errors += "Numele nu e valid!\n";
            nume.css("outline","red 2px solid");
        }
        else
            nume.css("outline","none");
    }

    var varsta = $("#varsta");
    if (varsta.val() == "") {
        errors += "Nu ati completat varsta!\n";
        varsta.css("outline","red 2px solid");
    }
    else{
        if (varsta.val()<=0)
        {
            errors += "Varsta trebuie sa fie pozitiva!\n";
            varsta.css("outline","red 2px solid");
        }
        else
            varsta.css("outline","none");
    }
    email = $("#email");
    if (email.val() == ""){
        errors += "Nu ati completat adresa de email!\n";
        email.css("outline","red 2px solid");
    }
    else{
        if(!(/^.+@.+\..+$/.test(email.val()))){
            errors += "Adresa de email invalida!\n";
            email.css("outline","red 2px solid");
        }
        else
            email.css("outline","none");
    }
    dataNasterii = $("#dataNasterii");
    if(dataNasterii.val() == ""){
        errors += "Nu ati ales data nasterii!\n";
        dataNasterii.css("outline","red 2px solid");
    }
    else
        dataNasterii.css("outline","none");
    if (errors == ""){
        alert("Toate campurile sunt completate corect");
        return true;
    }
    else{
        alert(errors);
        return false;
    }
}