const CACHE_NAME = 'home-automation-cache-v1';
const urlsToCache = [
  '/',
  '/index.html',
  '/Piku-Image.jpg',
  '/Arnab-Image.jpg',
  '/icon-192.png',
  '/icon-512.png',
  'https://cdn.tailwindcss.com',
  'https://cdnjs.cloudflare.com/ajax/libs/paho-mqtt/1.1.0/paho-mqtt.min.js',
  'https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap'
];

// Install the service worker and cache the app shell
self.addEventListener('install', event => {
  event.waitUntil(
    caches.open(CACHE_NAME)
      .then(cache => {
        console.log('Opened cache');
        return cache.addAll(urlsToCache);
      })
  );
});

// Serve cached content when offline
self.addEventListener('fetch', event => {
  event.respondWith(
    caches.match(event.request)
      .then(response => response || fetch(event.request))
  );
});