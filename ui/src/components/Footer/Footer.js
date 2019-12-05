import React from 'react';
import Contact from '../../containers/Contact/Contact';
import StoryFooter from '../StoryFooter/StoryFooter';
import './Footer.css';

const footer = () => {
    return (
        <div>
            <StoryFooter/>
            <footer className="Footer-Container-Div">
            <Contact />
            </footer>
        </div>
    );
}

export default footer;