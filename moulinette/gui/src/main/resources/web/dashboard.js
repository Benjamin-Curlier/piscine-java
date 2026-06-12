/* Tableau de bord : progression (modules → sous-groupes → exos), rapports moulinette,
 * raccourcis explorateur/terminal système. Rafraîchi après chaque commande du terminal web
 * (événement `piscine:refresh` émis par app.js). */
(function () {
  "use strict";

  const STATUS_ICON = { done: "✓", current: "●", locked: "🔒" };

  async function loadProgress() {
    const r = await fetch("/api/progress");
    const data = await r.json();
    const root = document.getElementById("modules");
    root.innerHTML = "";
    for (const mod of data.modules) {
      const exos = mod.sousGroupes.flatMap((sg) => sg.exos);
      const done = exos.filter((e) => e.status === "done").length;
      const isCurrent = exos.some((e) => e.status === "current");

      const card = document.createElement("details");
      card.className = "module-card" + (isCurrent ? " current" : "");
      card.open = isCurrent;

      const title = document.createElement("summary");
      title.textContent = "Module " + mod.module + " — " + done + "/" + exos.length + " exos validés";
      card.appendChild(title);

      for (const sg of mod.sousGroupes) {
        const block = document.createElement("div");
        block.className = "sous-groupe";
        const h = document.createElement("h3");
        h.textContent = "Sous-groupe " + sg.id;
        block.appendChild(h);
        const ul = document.createElement("ul");
        for (const exo of sg.exos) {
          const li = document.createElement("li");
          li.className = "exo " + exo.status;
          li.textContent = STATUS_ICON[exo.status] + " " + exo.id + " — " + exo.slug;
          ul.appendChild(li);
        }
        block.appendChild(ul);
        card.appendChild(block);
      }
      root.appendChild(card);
    }
  }

  async function loadReports() {
    const r = await fetch("/api/reports");
    const data = await r.json();
    const list = document.getElementById("reports-list");
    list.innerHTML = "";
    if (data.reports.length === 0) {
      const li = document.createElement("li");
      li.className = "muted";
      li.textContent = "Aucun rapport pour l'instant — pousse un rendu pour lancer la moulinette.";
      list.appendChild(li);
      return;
    }
    for (const name of data.reports) {
      const li = document.createElement("li");
      const a = document.createElement("a");
      a.href = "#";
      a.textContent = name;
      a.addEventListener("click", async (ev) => {
        ev.preventDefault();
        const md = await (await fetch("/api/report?name=" + encodeURIComponent(name))).text();
        const view = document.getElementById("report-view");
        view.classList.remove("muted");
        view.innerHTML = marked.parse(md);
      });
      li.appendChild(a);
      list.appendChild(li);
    }
  }

  function refresh() {
    loadProgress().catch(() => {});
    loadReports().catch(() => {});
  }

  /* Navigation sidebar */
  document.querySelectorAll(".nav-item[data-view]").forEach((btn) => {
    btn.addEventListener("click", () => {
      document.querySelectorAll(".nav-item[data-view]").forEach((b) => b.classList.remove("active"));
      btn.classList.add("active");
      document.getElementById("view-dashboard").classList.toggle("hidden", btn.dataset.view !== "dashboard");
      document.getElementById("view-terminal").classList.toggle("hidden", btn.dataset.view !== "terminal");
      if (btn.dataset.view === "dashboard") refresh();
      if (btn.dataset.view === "terminal" && window.piscineTerm) window.piscineTerm.focus();
    });
  });

  /* Lien cours si un site est servi (serveur dédié, URL absolue) */
  fetch("/api/state").then((r) => r.json()).then((s) => {
    if (s.coursesUrl) {
      const a = document.getElementById("nav-cours");
      a.href = s.coursesUrl;
      a.classList.remove("hidden");
    }
  }).catch(() => {});

  /* Raccourcis */
  function open(target) {
    fetch("/api/open", { method: "POST", body: JSON.stringify({ target }) }).catch(() => {});
  }
  document.getElementById("btn-explorer").addEventListener("click", () => open("explorer"));
  document.getElementById("btn-shell").addEventListener("click", () => open("terminal"));

  /* Rafraîchi quand le terminal web a exécuté une commande */
  window.addEventListener("piscine:refresh", refresh);

  refresh();
})();
