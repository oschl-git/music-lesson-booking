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
      "Content-Type": "application/json",
    },
    body: JSON.stringify(body),
  });

  if (!response.ok) {
    throw new Error(`POST request failed: ${response.status}`);
  }

  return await response.json();
}

async function makeDeleteRequest(endpoint) {
  const response = await fetch(endpoint, {
    method: "DELETE",
  });

  if (!response.ok) {
    throw new Error(`DELETE request failed: ${response.status}`);
  }
}

async function canTutorTeachPupilInstrument(
  instrument,
  tutor,
  pupilInstruments,
) {
  var pupilInstrument = pupilInstruments[instrument];

  if (pupilInstrument == null) {
    return false;
  }

  let tutorInstrument;
  for (const singleInstrument of tutor.instruments) {
    if (singleInstrument.instrument === instrument) {
      tutorInstrument = singleInstrument;
      break;
    }
  }

  if (tutorInstrument == null) {
    return false;
  }

  if (
    pupilInstrument.grade < tutorInstrument.minLevel ||
    pupilInstrument.grade > tutorInstrument.maxLevel
  ) {
    return false;
  }

  return true;
}

// https://stackoverflow.com/questions/33078406/getting-the-date-of-next-monday
function getNextMonday() {
  var date = new Date();

  date.setDate(date.getDate() + ((1 + 7 - date.getDay()) % 7 || 7));
  date.setHours(0, 0, 0, 0);

  return date;
}
