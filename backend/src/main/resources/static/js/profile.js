var page = 0;

function createThreadHtml(thread) {
  return `<a
    href="/t/${thread.name}"
    class="list-group-item list-group-item-action d-flex justify-content-between align-items-center"
  >
  ${thread.name}
    <span class="badge badge-primary badge-pill"
      >${thread.posts.length} posts</span
    >
  </a>`;
}

async function addNewElements() {
  showSpinner(true);

  const response = await fetch(
    "https://localhost:8443/thread/user/paginated?page=" +
      page +
      "&size=10" +
      "&username=" +
      username,
    {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    }
  );

  if (!response.ok) {
    throw new Error("Network response was not ok");
  }

  const data = await response.json();

  page += 1;
  if (page >= data.totalPages) {
    document.getElementById("loadMoreThreadsButton").style.display = "none";
  } else {
    document.getElementById("loadMoreThreadsButton").style.display = "block";
  }

  const container = document.getElementById("thread-container");

  showSpinner(false);

  data.content.forEach((thread) => {
    container.insertAdjacentHTML("beforeend", createThreadHtml(thread));
  });
}
