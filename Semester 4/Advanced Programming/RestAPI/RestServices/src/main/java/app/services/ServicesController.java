package app.services;

import app.model.Cursa;
import app.model.Cursa2;
import app.repository.CursaDbRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/mpp/curse")
public class ServicesController {

    @Autowired
    private CursaDbRepo cursaRepo;

    @RequestMapping(method= RequestMethod.GET)
    public MyList getAll(){
        MyList lista = new MyList();
        lista.setCurse(cursaRepo.findAll().stream().map(x->new Cursa2(x)).collect(Collectors.toList()));
        return lista;
    }

    @RequestMapping(value="/{id}",method = RequestMethod.GET)
    public ResponseEntity<?> findOne(@PathVariable Integer id){
        Cursa c = cursaRepo.findOne(id);
        if(c==null)
            return new ResponseEntity<String>("Not found!", HttpStatus.NOT_FOUND);
        else
            return new ResponseEntity<Cursa2>(new Cursa2(c),HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST)
    public Cursa2 save(@RequestBody Cursa2 cursa){
        cursaRepo.save(new Cursa(cursa));

        return cursa;
    }

    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public Cursa2 update(@PathVariable Integer id,@RequestBody Cursa2 cursa){
        cursa.setId(id);
        cursaRepo.update(new Cursa(cursa));
        return cursa;
    }

    @RequestMapping(value="/{id}",method = RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable Integer id){
        Cursa c= cursaRepo.findOne(id);
        if(c==null)
            return new ResponseEntity<String>("Not found!", HttpStatus.NOT_FOUND);
        else{
            cursaRepo.delete(id);
            return new ResponseEntity<Cursa>(HttpStatus.OK);
        }
    }

    @RequestMapping("/{id}/destination")
    public String destination(@PathVariable Integer id){
        return cursaRepo.findOne(id).getDestinatie();
    }
/*
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String userError(Exception e) {
        return e.getMessage();
    }

 */
}
