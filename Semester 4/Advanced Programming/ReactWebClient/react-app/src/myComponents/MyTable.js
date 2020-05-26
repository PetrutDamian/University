import React from "react";
import './myApp.css';
class MyRow extends React.Component{
    handleDelete = (event)=>{
        console.log("Click pe delete cursa:" +this.props.cursa);
        this.props.deleteFunc(this.props.cursa.id);
    }
    render() {
        return (
            <tr>
                <td>{this.props.cursa.id}</td>
                <td>{this.props.cursa.destinatie}</td>
                <td>{this.props.cursa.date}</td>
                <td>{this.props.cursa.locuriDisponibile}</td>
                <td><button className="deleteButton" onClick={this.handleDelete}>Delete</button></td>
            </tr>
        );
    }
}

export class MyTable extends React.Component{
    render() {
        let rows = [];
        let deleteFunc = this.props.deleteFunc;
        this.props.curse.forEach(function (cursa) {
            rows.push(<MyRow cursa={cursa} key={cursa.id} deleteFunc={deleteFunc}/>);
        });
        return (
            <div className="myTable">
                <table>
                    <thead>
                    <tr>
                        <th>ID</th>
                        <th>Destinatie</th>
                        <th>Data Plecarii</th>
                        <th>Numar Locuri</th>
                        <th>Action</th>
                    </tr>
                    </thead>
                    <tbody>{rows}</tbody>
                </table>
            </div>
        )
    }
}