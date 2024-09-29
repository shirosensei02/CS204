import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import 'bootstrap/dist/css/bootstrap.min.css';

function ViewTournament() {
    const [tournaments, setTournaments] = useState([]);

    useEffect(() => {
        const fetchTournaments = async () => {
            try {
                const response = await axios.get('http://localhost:8080/tournaments');
                setTournaments(response.data);
            } catch (error) {
                alert("Something went wrong while fetching tournaments.");
                console.error(error);
            }
        };

        fetchTournaments();
    }, []);

    const handleEdit = (id) => {
        console.log(`Editing tournament with id ${id}`);
    };

    const handleDelete = async (id) => {
        try {
            await axios.delete(`http://localhost:8080/tournaments/${id}`);
            setTournaments(tournaments.filter(tournament => tournament.id !== id));
            console.log(`Deleted tournament with id ${id}`);
        } catch (error) {
            alert(`Failed to delete tournament with id ${id}`);
            console.error(error);
        }
    };

    return (
        <div className="container">
            <h1>List of Tournaments</h1>
            <div className="list-group">
                {tournaments.map(tournament => (
                    <div key={tournament.id} className={`list-group-item list-group-item-action ${tournament.disabled ? 'disabled' : ''}`}>
                        <div className="d-flex justify-content-between">
                            <div>
                                <h5 className="mb-1">{tournament.name}</h5>
                                <small className="text-muted">{new Date(tournament.date).toLocaleString()}</small> {/* Display the date */}
                            </div>
                            <div className="d-flex">
                                <button className="btn btn-outline-primary me-2" onClick={() => handleEdit(tournament.id)}>Edit</button>
                                <button className="btn btn-outline-danger" onClick={() => handleDelete(tournament.id)}>Delete</button>
                            </div>
                        </div>
                    </div>
                ))}
            </div>
            <div className="mt-3">
                <Link to="/addTournament" className="btn btn-primary">Add Tournament</Link>
            </div>
        </div>
    );
}

export default ViewTournament;
