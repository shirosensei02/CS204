// src/components/AddTournament.js
import React, { useState, useEffect } from 'react';
import '@fortawesome/fontawesome-free/css/all.min.css';
import 'bootstrap/dist/css/bootstrap.min.css';
import axios from 'axios';

function AddTournament() {
  const [tname, setTname] = useState("");
  const [tdate, setTdate] = useState("");
  const [tregion, setTregion] = useState("asia");
  const [tstatus, setTstatus] = useState("");
  const[trrange, setTrange] = useState([]);


  const handleAddTournament = async (event) => {
    event.preventDefault();
    alert("Hey");
    if (tname && tdate && tstatus) {
      try {
        alert(tname + tdate + tregion + tstatus);
        
        const tournament = {
          name: tname,
          date: tdate,
          rankRange: [4, 7],
          status: tstatus,
          region: tregion,
          playerList: null
        };

        // Await the axios.post call
        const response = await axios.post('http://localhost:8080/tournaments', tournament, { withCredentials: true });
        console.log(response);
        //setAccount([account, response.data]);
        
        setTname(''); // Clear the input fields
        setTdate('');  
        setTregion('asia');
        setTstatus('');

        alert("Response: " + response.data);
      } catch (error) {
        console.error("There was an error adding the person!", error);
      }
    }
  };

  return (
    <div className="container">
      <div className="row justify-content-center">
        <div className="col-sm-9 col-md-7 col-lg-5">
          <div className="card border-0 shadow rounded-3 my-5">
            <div className="card-body p-4 p-sm-5">
              <h5 className="card-title text-center mb-5 fw-light fs-5">Sign In</h5>
              <form>
                <div className="form-floating mb-3">
                  <input type="text" className="form-control" id="floatingInput" value={tname} onChange={(e) => setTname(e.target.value)}/>
                  <label htmlFor="floatingInput">Tournament Name</label>
                </div>

                <div className="form-floating mb-3">
                  <input id="startDate" class="form-control" type="date" value={tdate} onChange={(e) => setTdate(e.target.value)}/>
                </div>

                <div className="form-floating mb-3">
                  <select name="region" id="regions" value={tregion} on={(e) => setTregion(e.target.value)}>
                    <option value="asia">Asia</option>
                    <option value="europe">Europe</option>
                    <option value="america">America</option>
                  </select>
                </div>

                <div className="form-floating mb-3">
                  <input type="text" className="form-control" id="floatingInput" value={tstatus} onChange={(e) => setTstatus(e.target.value)}/>
                  <label htmlFor="floatingInput">Status</label>
                </div>

                <div className="d-grid">
                  <button className="btn btn-primary btn-login text-uppercase fw-bold" onClick={handleAddTournament}>Add Tournament</button>
                </div>

              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

export default AddTournament;