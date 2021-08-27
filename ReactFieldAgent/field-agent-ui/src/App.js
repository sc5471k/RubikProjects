import React from "react";
import "./App.css";
import Input from "./components/Input";
import api from "./services/api.js";

function App() {
  // Are we updating?
  const [id2Edit, setId2Edit] = React.useState(null);

  // This is used to control the child component
  const [inputValue, setInputValue] = React.useState("");

  // Array destructuring
  const [agents, setAgents] = React.useState([]);

  React.useEffect(
    () => {
      api
        .index()
        .then((agents) => {
          setAgents(agents);
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

  const handleClick = ({ target }) => {
    const { dataset } = target;

    // Get the button text - which button was clicked?
    switch (target.innerText.toLowerCase()) {
      case "update":
        // We get the id from our data attribute in the button
        // We update state with this id
        // This will be checked when we submit so we can update, if necessary
        setId2Edit(Number(dataset.agents));

        // How do we find the correct text to use?
        setInputValue(
          agents.find(({ id }) => id === Number(dataset.agents)).description
        );
        break;
      case "delete":
        api.delete(dataset.agents).then(() => {
          setAgents((prevTodos) =>
            prevTodos.filter(({ id }) => id !== Number(dataset.agents))
          );
        });

        break;
      default:
        throw new Error("Invalid! Check your button text!");
    }
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    const form = e.target;

    const todoValue = form.elements[0].value.trim();

    if (todoValue) {
      // Check if this is an edit or a new todo
      if (id2Edit) {
        // We are updating
        api
          .update({ id: id2Edit, description: todoValue })
          .then(() => {
            setAgents((prevTodos) =>
              prevTodos.map((agent) => {
                if (agent.id === id2Edit) {
                  // Avoid mutating the original todo object
                  // We create a new object by spreading out the original (...todo)
                  // We compose the new object with the updated properties
                  // `inputValue` is piece of state
                  const updatedAgent = { ...agent, description: inputValue };
                  return updatedAgent;
                }

                return agent;
              })
            );
            // ⚠️ Don't get stuck in edit mode!
            setId2Edit(null);

            // Clear the input
            setInputValue("");
          })
          .catch((err) => {
            console.error("Some other error.", err);
          });
      }
      // CREATE!
      else {
        api
          .create({
            description: todoValue,
          })
          .then((response) =>
            setAgents((prevTodos) => [...prevTodos, response])
          );

        setInputValue("");
      }
    }
  };

  return (
    // Fragment tag
    <>
      <form onSubmit={handleSubmit} className="p-4">
        {/* Input gets re-rendered whenever inputValue changes. */}
        <Input value={inputValue} changeHandler={handleChange} />
        <button
          type="submit"
          className="bg-green-500 ml-1 p-4 rounded-sm text-white my-2"
        >
          {id2Edit ? "Update" : "Add"} Agent
        </button>
      </form>

      <ol className="p-4">
        {agents.map(({ id, description }) => (
          // TODO: Move this to a new component
          <li key={id} className="my-2">
            {description}{" "}
            <button
              className="bg-yellow-500 ml-1 rounded-xl p-2"
              onClick={handleClick}
              data-todo={id}
            >
              Update
            </button>
            <button
              className="bg-red-500 ml-1 rounded-xl text-white p-2"
              onClick={handleClick}
              data-todo={id}
            >
              Delete
            </button>
          </li>
        ))}
      </ol>
    </>
  );
}

export default App;