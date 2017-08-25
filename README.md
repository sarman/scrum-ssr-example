# Hacker News Clone


## Fork info

this is a fork of ssr example 
https://github.com/roman01la/scrum-ssr-example

roman01la was rename a project, to citrus,
so i do it too.

Just add figwheel, hot client code reload.

Not sure about "reloaded" workflow now,
but anyway, reload button is your friend)

Test it by change about page text

./run-dev 
and in another 
terminal run 
./figwheel

## Development

*start web app build*
```
lein cljsbuild auto dev
```

*start server*
```
rlwrap lein repl
(go)
```

## Project structure

- `client` — client-side (ClojureScript) only code
- `ssr` — backend (Clojure) code
- `ui` — shared UI code (*.cljc)

### Client

- `core.cljs` — app initialization
- `router.cljs` — hooking into HTML5 Hisotry API
- `effects.cljs` — effects handlers (HTTP)
- `controllers` — state management logic

### Server

- `core.clj` — app initialization
- `api.clj` — data retrieval from storage
- `page.clj` — HTML document template rendering
- `resolver.clj` — server state retrieval from api
- `middleware` — Ring middlewares: Transit format encoding/decoding, RPC API server, route matcher, web app renderer and Etag
- `components` — server components: web server, application
