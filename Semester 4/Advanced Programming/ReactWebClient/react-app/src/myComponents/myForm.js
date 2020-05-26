import React from "react";


export class MyForm extends React.Component{
    constructor(props) {
        super(props);
        this.state= {destinatie:'',data:'',nrLocuri:''};
    }

    handleSubmit = (event)=>{
        event.preventDefault();
        var cursa = {destinatie:this.state.destinatie,
        date:this.state.data,locuriDisponibile:this.state.nrLocuri};
        console.log("Submit : ",cursa);
        this.props.addFunc(cursa);
    };
    handleChange = (event, fieldName)=>{
        this.setState({[fieldName]:event.target.value});
    };
    render() {
        return (
            <form onSubmit={this.handleSubmit}>
                <label>
                    Destinatie:
                    <input type="text"  value={this.state.destinatie} onChange={
                        (event) => this.handleChange(event,'destinatie')}
                    />
                </label><br/>
                <label>
                    Data:
                    <input type="text" placeholder="yyyy-MM-dd HH:mm" value={this.state.data} onChange={
                        (event)=>this.handleChange(event,'data') }
                    />
                </label><br/>
                <label>
                    NumarLocuri:
                    <input type="text" value={this.state.nrLocuri} onChange={
                        (event)=>this.handleChange(event,'nrLocuri') }
                    />
                </label><br/>
                <input className="submitButton" type="submit" value="save"/>
            </form>
        );
    }
}