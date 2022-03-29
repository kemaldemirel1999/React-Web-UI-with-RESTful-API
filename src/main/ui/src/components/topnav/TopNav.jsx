import React from 'react'

import './topnav.css'

import { Link , useHistory} from 'react-router-dom'

import Dropdown from '../dropdown/Dropdown'

import ThemeMenu from '../thememenu/ThemeMenu'

import user_image from '../../assets/images/user.png'

import user_menu from '../../assets/JsonData/user_menus.json'

const curr_user = {
    display_name: 'Kemal Demirel',
    image: user_image
}


const renderUserToggle = (user) => (
    <div className="topnav__right-user">
        <div className="topnav__right-user__image">
            <img src={user.image} alt="" />
        </div>
        <div className="topnav__right-user__name">
            {user.display_name}
        </div>
    </div>
)

const renderUserMenu =(item, index) => (
    <Link to='/settings' key={index}>
        <div className="notification-item">
            <i className={item.icon}></i>
            <span>{item.content}</span>
        </div>
    </Link>
)




const Topnav = () => {

    const initialFormData = Object.freeze({
        scanId: "",
    });

    const history = useHistory();

    const [formData, updateFormData] = React.useState(initialFormData);

    const handleChange = (e) => {
        updateFormData({
            ...formData,

            // Trimming any whitespace
            [e.target.name]: e.target.value.trim()
        });
    };

    const handlePress = (e) => {

        if(e.key == 'Enter'){
            console.log(formData.scanId)
            fetch("http://localhost:8080/api/search-by-id",{
                method: "POST",
                headers:{"Content-Type":"application/json" },
                body: JSON.stringify(formData.scanId)
            }).then( () =>{
                    console.log("Search is called")
                }
            )
            history.push("/results")
        }
    }

    return (
        <div className='topnav'>
            <div className="topnav__search">
                <input type="text" name="scanId" placeholder='Search Details By Scan Id'  onChange={handleChange} onKeyPress={handlePress}/>

            </div>
            <div className="topnav__right">
                <div className="topnav__right-item">
                    {/* dropdown here */}
                    <Dropdown
                        customToggle={() => renderUserToggle(curr_user)}
                        contentData={user_menu}
                        renderItems={(item, index) => renderUserMenu(item, index)}
                    />
                </div>
                
                <div className="topnav__right-item">
                    <ThemeMenu/>
                </div>
            </div>
        </div>
    )
}

export default Topnav
