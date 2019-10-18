import React from 'react';
import './StoryFooter.css';
import foot from './foot.png';


const storyFooter = () => {
    return (
        <div >
            <img className="Container-footer-img" src={foot}/>
            <div className="Paragraph-footer">
                <p>OUR STORY </p>
            </div>
            <div className="Empty-Div-footer" />
        </div>
    );
}

export default storyFooter;