const ctx = window.location.pathname.split('/')[1];
const base = `${window.location.origin}/${ctx}`;

// Alerts
function showAlert(containerId, type, msg) {
  const ph = document.getElementById(containerId);
  const alert = document.createElement('div');
  alert.innerHTML = `
    <div class="alert alert-${type} alert-dismissible fade show" role="alert">
      ${msg}
      <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
    </div>`;
  ph.append(alert);
}

// Fetch helpers
async function api(url, opts={}) {
  const res = await fetch(base + url, opts);
  const json = await res.json();
  if (!res.ok) throw new Error(json.error || res.status);
  return json;
}

// Cargas iniciales
async function loadMarcas(selectEl, includeAll=true) {
  const marcas = await api('/api/marcas');
  selectEl.innerHTML = includeAll
    ? `<option value="">-- Selecciona Marca --</option>`
    : '';
  marcas.forEach(m => {
    selectEl.add(new Option(m.nombre, m.id));
  });
}

async function loadModelos(selectEl, marcaId) {
  selectEl.innerHTML = `<option value="">-- Selecciona Modelo --</option>`;
  selectEl.disabled = !marcaId;
  if (marcaId) {
    const modelos = await api(`/api/modelos/marca/${marcaId}`);
    modelos.forEach(mo => selectEl.add(new Option(mo.nombre, mo.id)));
  }
}

async function loadColores() {
  const colores = await api('/api/colores');
  const sel = document.getElementById('v-color');
  sel.innerHTML = `<option value="">Color</option>`;
  colores.forEach(c => sel.add(new Option(c.nombre, c.id)));
}

// Listados
async function refreshVehiculos() {
  const data = await api('/api/vehiculos');
  const tb = document.querySelector('#table-vehiculos tbody');
  tb.innerHTML = '';
  data.forEach(v => {
    tb.insertRow().innerHTML = `
      <td>${v.id}</td>
      <td>${v.idMarca}</td>
      <td>${v.idModelo}</td>
      <td>${v.placa}</td>
      <td>${v.chasis}</td>
      <td>${v.anio}</td>
      <td>${v.idColor}</td>
      <td>${new Date(v.fechaCreacion).toLocaleDateString()}</td>`;
  });
}
async function refreshList(idList, apiPath) {
  const list = await api(apiPath);
  const ul = document.getElementById(idList);
  ul.innerHTML = '';
  list.forEach(item => {
    const li = document.createElement('li');
    li.className = 'list-group-item d-flex justify-content-between align-items-center';
    li.textContent = item.nombre;
    ul.append(li);
  });
}

// Eventos al cargar
window.addEventListener('load', async () => {
  // Vehículos
  await Promise.all([
    loadMarcas(document.getElementById('v-marca')),
    loadColores(),
    refreshVehiculos()
  ]);
  // Marcas y modelos
  await loadMarcas(document.getElementById('mo-marca'));
  await Promise.all([
    refreshList('list-marcas', '/api/marcas'),
    refreshList('list-modelos', '/api/modelos'),
    refreshList('list-colores', '/api/colores')
  ]);
});

// Dependencia Marca→Modelo en Vehículos
document.getElementById('v-marca').addEventListener('change', e => {
  loadModelos(document.getElementById('v-modelo'), e.target.value);
});

// --- Form submissions ---

// Vehículos
document.getElementById('form-vehiculos').addEventListener('submit', async e => {
  e.preventDefault(); e.target.classList.add('was-validated');
  if (!e.target.checkValidity()) return;
  const data = {
    idMarca:  +e.target['v-marca'].value,
    idModelo: +e.target['v-modelo'].value,
    placa:    e.target['v-placa'].value.trim().toUpperCase(),
    chasis:   e.target['v-chasis'].value.trim().toUpperCase(),
    anio:     e.target['v-anio'].value,
    idColor:  +e.target['v-color'].value
  };
  try {
    const res = await api('/api/vehiculos', {
      method: 'POST',
      headers: {'Content-Type':'application/json'},
      body: JSON.stringify(data)
    });
    showAlert('alert-vehiculos','success', res.message);
    e.target.reset(); e.target.classList.remove('was-validated');
    await refreshVehiculos();
  } catch(err) {
    showAlert('alert-vehiculos','danger', err.message);
  }
});

// Marcas
document.getElementById('form-marcas').addEventListener('submit', async e => {
  e.preventDefault(); e.target.classList.add('was-validated');
  if (!e.target.checkValidity()) return;
  const nombre = e.target['m-nombre'].value.trim();
  try {
    await api('/api/marcas', {
      method:'POST', headers:{'Content-Type':'application/json'},
      body: JSON.stringify({nombre})
    });
    showAlert('alert-marcas','success','Marca creada');
    e.target.reset(); e.target.classList.remove('was-validated');
    await Promise.all([
      loadMarcas(document.getElementById('v-marca')),
      loadMarcas(document.getElementById('mo-marca')),
      refreshList('list-marcas','/api/marcas')
    ]);
  } catch(err) {
    showAlert('alert-marcas','danger', err.message);
  }
});

// Modelos
document.getElementById('form-modelos').addEventListener('submit', async e => {
  e.preventDefault(); e.target.classList.add('was-validated');
  if (!e.target.checkValidity()) return;
  const idMarca = +e.target['mo-marca'].value;
  const nombre  = e.target['mo-nombre'].value.trim();
  try {
    await api('/api/modelos', {
      method:'POST', headers:{'Content-Type':'application/json'},
      body: JSON.stringify({idMarca, nombre})
    });
    showAlert('alert-modelos','success','Modelo creado');
    e.target.reset(); e.target.classList.remove('was-validated');
    await Promise.all([
      loadModelos(document.getElementById('v-modelo'), document.getElementById('v-marca').value),
      loadModelos(document.getElementById('mo-modelo'), idMarca), // if exists
      refreshList('list-modelos','/api/modelos')
    ]);
  } catch(err) {
    showAlert('alert-modelos','danger', err.message);
  }
});

// Colores
document.getElementById('form-colores').addEventListener('submit', async e => {
  e.preventDefault(); e.target.classList.add('was-validated');
  if (!e.target.checkValidity()) return;
  const nombre = e.target['c-nombre'].value.trim();
  try {
    await api('/api/colores', {
      method:'POST', headers:{'Content-Type':'application/json'},
      body: JSON.stringify({nombre})
    });
    showAlert('alert-colores','success','Color creado');
    e.target.reset(); e.target.classList.remove('was-validated');
    await Promise.all([
      loadColores(),
      refreshList('list-colores','/api/colores')
    ]);
  } catch(err) {
    showAlert('alert-colores','danger', err.message);
  }
});
