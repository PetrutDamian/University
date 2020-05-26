import React from "react"
import {MyForm} from "./myForm";
import './myApp.css'
import {MyTable} from "./MyTable";
import {RestCaller} from "../Rest/RestCalls";

export class MyApp extends React.Component{
    constructor(props) {
        super(props);
        this.state ={
            curse:[]
        }
    }
    addFunction = (cursa)=>{
        if(cursa.destinatie=='' || cursa.date=="" || isNaN(cursa.locuriDisponibile))
            alert("invalid fields!");
        else {
            console.log("Enter addFunction with:" + cursa);
            RestCaller.addCursa(cursa).then(res => RestCaller.getCurse()).then(curse => {
                this.setState(curse);
            }).catch(error => {
                console.log("Eroare la add.", error);
            });
        }
    };

    deleteFunction = (cursaId)=>{
        console.log("Enter deleteFunction with: "+cursaId);
        RestCaller.deleteCursa(cursaId).then(res=>
            RestCaller.getCurse()).then(curse=>{
                console.log("Am cerut cursele");
                this.setState(curse)}).catch(error=>{
            console.log("eroare la delete.",error);
        });
    };

    componentDidMount() {
        console.log("Componenent Mounted...");
        RestCaller.getCurse().then(curse=>this.setState(curse));
    }

    render() {
        return (
            <div className="myApp">
                <h1>React Management App</h1><br/>
                <MyForm addFunc={this.addFunction} /><br/><br/>
                <MyTable curse={this.state.curse} deleteFunc={this.deleteFunction}/>
            </div>
        )
    }

}