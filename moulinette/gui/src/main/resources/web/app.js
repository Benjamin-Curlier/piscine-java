/* Terminal web de la Piscine ETNC : chaque ligne validée part en POST /api/terminal,
 * la réponse (CommandResult) s'affiche. Le prompt est rendu côté client. */
(function () {
  "use strict";

  const BACKSPACE = String.fromCharCode(127); // DEL, envoyé par xterm.js
  const term = new Terminal({
    cursorBlink: true,
    fontSize: 15,
    fontFamily: "Consolas, 'Courier New', monospace",
    theme: { background: "#1e1e2e", foreground: "#cdd6f4" },
  });
  term.open(document.getElementById("terminal"));

  let branch = "?";
  let buffer = "";
  let busy = false;
  let closed = false;

  function writeLines(text) {
    term.write(text.replace(/\r?\n/g, "\r\n"));
  }

  function prompt() {
    term.write("piscine[" + branch + "]> ");
  }

  async function refreshState() {
    try {
      const r = await fetch("/api/state");
      const s = await r.json();
      branch = s.branch;
      document.getElementById("workspace").textContent = "workspace : " + s.repoRoot;
    } catch (e) {
      /* serveur arrêté : on garde l'état courant */
    }
  }

  async function run(line) {
    busy = true;
    try {
      const r = await fetch("/api/terminal", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ cmd: line }),
      });
      const res = await r.json();
      if (res.output && res.output.length > 0) {
        writeLines(res.output);
        if (!res.output.endsWith("\n")) term.write("\r\n");
      }
      if (res.shouldExit) {
        closed = true;
        writeLines("Session terminée. Tu peux fermer cet onglet.\n");
        return;
      }
      await refreshState();
      window.dispatchEvent(new Event("piscine:refresh"));
    } catch (e) {
      writeLines("\nErreur de communication avec l'application locale.\n");
    } finally {
      busy = false;
      if (!closed) prompt();
    }
  }

  term.onData(function (data) {
    if (busy || closed) return;
    for (const ch of data) {
      if (ch === "\r") {
        term.write("\r\n");
        const line = buffer.trim();
        buffer = "";
        if (line.length === 0) { prompt(); continue; }
        run(line);
      } else if (ch === BACKSPACE || ch === "\b") {
        if (buffer.length > 0) {
          buffer = buffer.slice(0, -1);
          term.write("\b \b");
        }
      } else if (ch >= " ") {
        buffer += ch;
        term.write(ch);
      }
    }
  });

  window.piscineTerm = term;

  (async function init() {
    await refreshState();
    writeLines("Piscine ETNC — console locale. Tape `help` pour la liste des commandes.\n");
    prompt();
    term.focus();
  })();
})();
