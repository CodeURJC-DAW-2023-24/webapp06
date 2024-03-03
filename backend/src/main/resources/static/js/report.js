async function validatePost(validateButton) {
  let postId = validateButton.getAttribute("data-id");

  const response = await fetch("/p/validate/" + postId, {
    method: "GET",
  });

  if (!response.ok) {
    throw new Error("Network response was not ok");
  }
}

function createThreadHtml(post) {
  return `<div
  class="list-group-item d-flex align-items-center justify-content-between"
>
  <div class="d-flex align-items-center">
    <img
      src="/image/user/${post.owner.username}"
      class="rounded-circle"
      style="width: 50px; height: 50px"
      alt="User Photo"
    />
    <div class="ml-3 text-truncate">
      <strong>${post.text}</strong>
    </div>
  </div>
  <div>
    <div>
      <span class="badge badge-primary badge-pill ml-2"
        >${post.reports} Reports</span
      >
      <a
        class="btn btn-link text-primary p-0 mr-2"
        title="View Profile"
        style="text-decoration: none"
      >
        <i class="fas fa-eye"></i>
      </a>
      <button
        class="btn btn-link text-primary p-0 mr-2"
        title="Validate"
        style="text-decoration: none"
        onclick="validatePost(${post});"
        data-id="${post.id}"
      >
        <i class="fas fa-check"></i>
      </button>
    </div>
  </div>
</div>`;
}

function getPosts() {
  const url = "https://localhost:8443/p/reports" + "?page=" + page + "&size=10";
  addNewElements(createThreadHtml, url, "loadMoreButton", "post-container");
}

function getMoreElements() {
  getPosts();
}

document.addEventListener("DOMContentLoaded", function () {
  getPosts();
});
