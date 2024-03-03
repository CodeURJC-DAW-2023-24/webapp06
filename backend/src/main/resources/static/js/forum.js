

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

function getThreads() {
  const url =
    "https://localhost:8443/thread/paginated?page=" + page + "&size=10" + "&nameForum=" + nameForum;
  addNewElements(createThreadHtml, url, "loadMoreButton", "thread-container");
}

function getMoreElements() {
  getThreads();
}
