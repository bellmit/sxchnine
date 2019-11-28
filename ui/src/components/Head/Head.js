import React from 'react';
import jordan from './jordan2.png';
import logo from './logo_got_it.png';
import './Head.css';

const head = () => {
    return (
      <div className="Header-Body">
          <div>
              <img alt ="" className="Container-img" src={jordan}/>
          </div>
          <div>
              <p className="Paragraph">YOU KNOW MY _STEELO !</p>
          </div>
          <div className="Empty-Div" />

         <div>
              <img alt ="" className="Got-it-logo" src={logo}/>
          </div>
      </div>
    );
}

export default head;