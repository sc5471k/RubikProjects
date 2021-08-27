import PropTypes from "prop-types";
import React from "react";

function Input({ changeHandler, value }) {
  return (
    <div>
      <label className="sr-only" htmlFor="add-todo">
        Add Todo
      </label>
      <input id="add-todo" type="text" onChange={changeHandler} value={value} />
    </div>
  );
}

Input.propTypes = {
  changeHandler: PropTypes.func.isRequired,
  value: PropTypes.string.isRequired,
};

export default Input;
