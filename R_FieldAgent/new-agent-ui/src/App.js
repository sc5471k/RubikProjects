import React from "react";
import './App.css';
import { useState, useEffect } from 'react';
import api from "./services/api.js";

function App() {
    // This is used to control the child component
    const [inputValue, setInputValue] = React.useState("");
  const [agents, setAgents] = useState([]);
  const [agent, setAgent] = useState({
    firstName: "",
    middleName: "",
    lastName: "",
    dob: "",
    heightInInches: null,
  });

  React.useEffect(
    () => {
      api
        .index()
        .then((todos) => {
          setAgents(todos);
        })
        .catch(() => {
          console.error("Some other error when fetching all the stuff");
        });
    },

    // Dependency array
    // Empty means only run this `useEffect` once
    []
  );

  const handleChange = (e) => {
    // As we type in the input, we want to update the state
    // This will updated the controlled Input component
    setInputValue(e.target.value);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const form = e.target;

    const firstNameValue = form.elements[0].value.trim();
    const middleNameValue = form.elements[1].value.trim();
    const lastNameValue = form.elements[2].value.trim();
    const dobValue = form.elements[3].value.trim();
    const heightValue = form.elements[4].value.trim();
        api
          .create({
            firstName: firstNameValue,
            middleName: middleNameValue,
            lastName: lastNameValue,
            dob: dobValue,
            heightInInches: heightValue
          })
          .then((response) =>
            setAgents((prevAgents) => [...prevAgents, response])
          );

        setInputValue("");
  };


  async function getAll() {
    const init = {
        method: "GET",
        headers: {
            "Accept": "application/json"
        }
    };

    const response = await fetch("http://localhost:8080/api/agent", init);
    if (response.status !== 200) {
        console.log(`Bad status: ${response.status}`);
        return Promise.reject("response is not 200 OK");
    }
    const json = await response.json();

    // Add data to the DOM.
    console.log()
    setAgents(json);

    let html = "";
    for (const agent of json) {
        html += `<div><strong>${agent.firstName}</strong> ${agent.lastName}</div>`
    }
    document.getElementById("results").innerHTML = html;
}

  // async function addAgent(evt) {
  //   evt.preventDefault();

  //   // document.getElementById("firstName").value = agent.firstName;
  //   // document.getElementById("middleName").value = agent.middleName;
  //   // document.getElementById("lastName").value = agent.lastName;
  //   // document.getElementById("dob").value = agent.dob;
  //   // document.getElementById("heightInInches").value = agent.heightInInches;

  //   const agent = {
  //     "firstName": document.getElementById("firstName").value,
  //     "middleName": document.getElementById("middleName").value,
  //     "lastName": document.getElementById("lastName").value,
  //     "dob": document.getElementById("dob").value,
  //     "heightInInches": document.getElementById("heightInInches").value
  //   }

  //   const init = {
  //     method: "POST",
  //     headers: {
  //         "Content-Type": "application/json",
  //         "Accept": "application/json"
  //     },
  //     body: JSON.stringify(agent)
  // };

  // fetch("http://localhost:8080/api/agent", init)
  //     .then(response => {
  //         if (response.status !== 201) {
  //             return Promise.reject("response is not 200 OK");
  //         }
  //         return response.json();
  //     })
  //     .then(json => setAgents([...agents, json])) // Spread new state
  //     .catch(console.log);
  // };

  async function addAgentForm() {
    let html = "";
    html += `<form onsubmit="alert('Agent added');return false;">
            First Name: <input type="text" id="firstName" name="firstName"> <br>
            Middle Name: <input type="text" id="lastName" name="lastName"> <br>
            Last Name: <input type="text" id="lastName" name="lastName"> <br>
            DOB: <input type="text" id="dob" name="dob"> <br>
            Height (In): <input type="text" id="height" name="height"> <br>
            <button onClick={addAgent} type="submit">Submit</button>
            </form>`
    
    document.getElementById("results").innerHTML = html;
    document.querySelector("form").addEventListener("submit", handleSubmit);
  }
  
  async function updateAgent() {
  
  }
  
  async function doDelete() {
  
  }

  return (
    <div className="App">
      <header className="App-header">
        <p>Agent</p>
        <form className="menu">
        <div>
          <button onClick={getAll} type="button">Get: Agent</button> &#160;
          <button onClick={addAgentForm} type="button">Add: Agent</button> &#160;
          <button onClick={updateAgent} type="button">Update: Agent</button> &#160;
          <button onClick={doDelete} type="button">Delete: Agent</button> &#160;
        </div>
      </form>
      <p id="results"></p>
      </header>
    </div>
  );
}

export default App;

