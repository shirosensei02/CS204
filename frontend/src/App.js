import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import AddTournament from './components/AddTournament';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/addTournament" element={<AddTournament/>} />
      </Routes>
    </Router>
  );
}

export default App;

