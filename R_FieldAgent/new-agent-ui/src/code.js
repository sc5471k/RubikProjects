async function getAll() {

    const init = {
        method: "GET",
        headers: {
            "Accept": "application/json"
        }
    };

    fetch("http://localhost:8080/agent", init)
        .then(response => {
            if (response.status !== 200) {
                console.log(`Bad status: ${response.status}`);
                return Promise.reject("response is not 200 OK");
            }
            return response.json();
        })
        .then(json => console.log(json));
}

async function getById() {

}

async function post() {

}

async function put() {

}

// `delete` is a JavaScript keyword
// so we use `doDelete` instead.
async function doDelete() {

}