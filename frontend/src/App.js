import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { BrowserRouter as Router, Routes, Route, Link } from 'react-router-dom';
import AddTournament from './components/AddTournament';
import ViewTournament from './components/ViewTournament';

function App() {
  return (
    <Router>
      <Routes>
        <Route path="/addTournament" element={<AddTournament />} />
        <Route path="/viewTournaments" element={<ViewTournament />} />
      </Routes>
    </Router>
  );
}

export default App;

