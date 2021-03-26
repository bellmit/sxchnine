import React from 'react';
import Contact from '../../containers/Contact/Contact';
import StoryFooter from '../StoryFooter/StoryFooter';
import Aux from '../../hoc/Aux/Aux';
import './Footer.css';

const footer = (props) => {
    return (
        <Aux>
            <StoryFooter {...props}/>
            <Contact />
        </Aux>
    );
}

export default footer;