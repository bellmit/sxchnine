import React from 'react';
import Contact from '../Contact/Contact';
import StoryFooter from '../StoryFooter/StoryFooter';
import './Footer.css';

const footer = () => {
    return (
        <div>
            <StoryFooter/>
            <footer>
            <Contact />
            </footer>
        </div>
    );
}

export default footer;