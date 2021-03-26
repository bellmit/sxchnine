import React from 'react';
import './StoryFooter.css';
import Aux from '../../hoc/Aux/Aux';
import foot from './foot.png';


const storyFooter = (props) => {
    return (
        <Aux>
            <img alt="" className="Container-footer-img" src={foot}/>
            <p className="Paragraph-footer" onClick={() => props.history.push('/ourStory')}>OUR STORY </p>
            <div className="Empty-Div-footer"/>
        </Aux>
    );
}

export default storyFooter;