async function makeGetRequest(endpoint) {
  const response = await fetch(endpoint);

  if (!response.ok) {
      throw new Error(`GET request failed: ${response.status}`);
  }

  return await response.json();
}

async function makePostRequest(endpoint, body) {
  const response = await fetch(endpoint, {
      method: "POST",
      headers: {
          "Content-Type": "application/json"
      },
      body: JSON.stringify(body),
  });

  if (!response.ok) {
      throw new Error(`POST request failed: ${response.status}`);
  }

  return await response.json();
}

async function makeDeleteRequest(endpoint) {
  const response = await fetch(endpoint, {method: "DELETE"});

  if (!response.ok) {
      throw new Error(`DELETE request failed: ${response.status}`);
  }
}
